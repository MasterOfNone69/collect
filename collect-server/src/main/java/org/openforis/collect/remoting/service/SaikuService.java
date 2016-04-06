package org.openforis.collect.remoting.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.granite.context.GraniteContext;
import org.granite.messaging.webapp.HttpGraniteContext;
import org.openforis.collect.concurrency.CollectJobManager;
import org.openforis.collect.concurrency.SurveyLockingJob;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.reporting.ReportingRepositories;
import org.openforis.collect.reporting.SaikuConfiguration;
import org.openforis.collect.reporting.proxy.ReportingRepositoryInfoProxy;
import org.openforis.collect.utils.Proxies;
import org.openforis.concurrency.Progress;
import org.openforis.concurrency.ProgressListener;
import org.openforis.concurrency.Task;
import org.openforis.concurrency.proxy.JobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author S. Ricci
 *
 */
@Component
public class SaikuService {

	private static final String SAIKU_URL_FORMAT = "%s://%s:%s/%s";
	private static final String DEV_LOCAL_ADDRESS = "127.0.0.1";
	private static final String DEV_REQUEST_LOCAL_ADDRESS = "0:0:0:0:0:0:0:1";
	
	@Autowired
	private ReportingRepositories reportingRepositories;
	@Autowired
	private SurveyManager surveyManager;
	@Autowired
	private CollectJobManager jobManager;
	@Autowired
	private SaikuConfiguration saikuConfiguration;
	
	public boolean isSaikuAvailable() {
		String url = getSaikuUrl();
		return testUrl(url);
	}
	
	public ReportingRepositoryInfoProxy loadInfo(String surveyName) {
		return Proxies.fromObject(reportingRepositories.getInfo(surveyName), ReportingRepositoryInfoProxy.class);
	}
	
	public JobProxy generateRdb(final String surveyName) {
		SurveyLockingJob job = new SurveyLockingJob() {
			protected void buildTasks() throws Throwable {
				addTask(new Task() {
					protected void execute() throws Throwable {
						reportingRepositories.createRepositories(surveyName, new ProgressListener() {
							public void progressMade(Progress progress) {
								setProcessedItems(progress.getProcessedItems());
								setTotalItems(progress.getTotalItems());
							}
						});
					}
				});
			}
		};
		CollectSurvey survey = surveyManager.get(surveyName);
		job.setSurvey(survey);
		jobManager.startSurveyJob(job);
		return new JobProxy(job);
	}

	public String getSaikuUrl() {
		HttpGraniteContext graniteContext = (HttpGraniteContext) GraniteContext.getCurrentInstance();
		HttpServletRequest request = graniteContext.getRequest();
		String protocol = request.isSecure() ? "https" : "http";
		String localAddr = request.getLocalAddr();
		if (DEV_REQUEST_LOCAL_ADDRESS.equals(localAddr)) {
			localAddr = DEV_LOCAL_ADDRESS;
		}
		String url = String.format(SAIKU_URL_FORMAT, protocol, localAddr, request.getLocalPort(), saikuConfiguration.getContextPath());
		return url;
	}

	private boolean testUrl(String url) {
		try {
			HttpClientBuilder cb = HttpClientBuilder.create();
			CloseableHttpClient httpClient = cb.build();
			HttpHead req = new HttpHead(url);
			CloseableHttpResponse resp = httpClient.execute(req);
			int statusCode = resp.getStatusLine().getStatusCode();
			return statusCode == 200;
		} catch (Exception e) {
			return false;
		}
	}

}
