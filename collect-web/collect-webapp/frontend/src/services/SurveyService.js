import AbstractService from './AbstractService';
import { Survey } from '../model/Survey';

export default class SurveyService extends AbstractService {

    fetchById(surveyId) {
        return this.get('survey/' + surveyId).then(res => new Survey(res))
    }

    createSurvey(name, templateType, userGroupId) {
        return this.post('survey', {
            name: name,
            templateType: templateType,
            userGroupId: userGroupId
        })
    }

    changeUserGroup(surveyId, userGroupId, loggedUserId) {
        return this.post('survey/changeusergroup/' + surveyId, {
            userGroupId: userGroupId,
            loggedUserId: loggedUserId
        }).then(res => res)
    }
    
    fetchAllSummaries() {
        return this.get('survey')
    }
}