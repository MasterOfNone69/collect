package org.openforis.collect.client {
	
	/**
	 * 
	 * @author Mino Togna
	 * @author Stefano Ricci
	 * @author E. Wibowo
	 * */
	public class ClientFactory {
		
		private static var _collectJobClient:CollectJobClient;
		private static var _codeListClient:CodeListClient;
		private static var _codeListImportClient:CodeListImportClient;
		private static var _configurationClient:ConfigurationClient;
		private static var _csvDataImportClient:CSVDataImportClient;
		private static var _dataClient:DataClient;
		private static var _dataExportClient:DataExportClient;
		private static var _dataImportClient:DataImportClient;
		private static var _logoClient:LogoClient;
		private static var _modelClient:ModelClient;
		private static var _saikuClient:SaikuClient;
		private static var _samplingDesignClient:SamplingDesignClient;
		private static var _samplingDesignImportClient:SamplingDesignImportClient;
		private static var _sessionClient:SessionClient;
		private static var _speciesClient:SpeciesClient;
		private static var _speciesImportClient:SpeciesImportClient;
		private static var _userClient:UserClient;
		private static var _userSessionClient:UserSessionClient;
		
		public static function get collectJobClient():CollectJobClient {
			if(_collectJobClient == null){
				_collectJobClient = new CollectJobClient();
			}
			return _collectJobClient;
		}
		
		public static function get codeListClient():CodeListClient {
			if(_codeListClient == null){
				_codeListClient = new CodeListClient();
			}
			return _codeListClient;
		}

		public static function get codeListImportClient():CodeListImportClient {
			if(_codeListImportClient == null){
				_codeListImportClient = new CodeListImportClient();
			}
			return _codeListImportClient;
		}
		
		public static function get configurationClient():ConfigurationClient {
			if(_configurationClient == null){
				_configurationClient = new ConfigurationClient();
			}
			return _configurationClient;
		}
		
		public static function get csvDataImportClient():CSVDataImportClient {
			if( _csvDataImportClient == null){
				_csvDataImportClient = new CSVDataImportClient();
			}
			return _csvDataImportClient;
		}
		
		public static function get dataClient():DataClient {
			if(_dataClient == null){
				_dataClient = new DataClient();
			}
			return _dataClient;
		}

		public static function get dataExportClient():DataExportClient {
			if(_dataExportClient == null) {
				_dataExportClient = new DataExportClient();
			}
			return _dataExportClient;
		}
		
		public static function get dataImportClient():DataImportClient {
			if(_dataImportClient == null) {
				_dataImportClient = new DataImportClient();
			}
			return _dataImportClient;
		}

		public static function get modelClient():ModelClient {
			if(_modelClient == null){
				_modelClient = new ModelClient();
			}
			return _modelClient;
		}
		
		public static function get logoClient():LogoClient {
			if(_logoClient == null){
				_logoClient = new LogoClient();
			}
			return _logoClient;
		}
		
		public static function get samplingDesignImportClient():SamplingDesignImportClient {
			if(_samplingDesignImportClient == null){
				_samplingDesignImportClient = new SamplingDesignImportClient();
			}
			return _samplingDesignImportClient;
		}
		
		public static function get samplingDesignClient():SamplingDesignClient {
			if(_samplingDesignClient == null){
				_samplingDesignClient = new SamplingDesignClient();
			}
			return _samplingDesignClient;
		}

		public static function get saikuClient():SaikuClient {
			if(_saikuClient == null){
				_saikuClient = new SaikuClient();
			}
			return _saikuClient;
		}
		
		public static function get sessionClient():SessionClient {
			if(_sessionClient == null){
				_sessionClient = new SessionClient();
			}
			return _sessionClient;
		}
		
		public static function get speciesClient():SpeciesClient {
			if(_speciesClient == null){
				_speciesClient = new SpeciesClient();
			}
			return _speciesClient;
		}
		
		public static function get speciesImportClient():SpeciesImportClient {
			if(_speciesImportClient == null){
				_speciesImportClient = new SpeciesImportClient();
			}
			return _speciesImportClient;
		}
		
		public static function get userClient():UserClient {
			if(_userClient == null){
				_userClient = new UserClient();
			}
			return _userClient;
		}
		
		public static function get userSessionClient():UserSessionClient {
			if(_userSessionClient == null){
				_userSessionClient = new UserSessionClient();
			}
			return _userSessionClient;
		}
		
	}
}