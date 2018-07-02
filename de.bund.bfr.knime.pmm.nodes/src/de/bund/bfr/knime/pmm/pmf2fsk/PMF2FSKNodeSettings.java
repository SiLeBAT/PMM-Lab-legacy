package de.bund.bfr.knime.pmm.pmf2fsk;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
public class PMF2FSKNodeSettings {

	private static final String CFG_FILE = "file";

	  String filePath = "";

	  void load(final NodeSettingsRO settings) throws InvalidSettingsException {
	    filePath = settings.getString(CFG_FILE);
	    
	    outPath = settings.getString(CFG_OUT_PATH);
		modelName = settings.getString(CFG_MODEL_NAME);
		creatorGivenName = settings.getString(CFG_CREATOR_GIVEN_NAME);
		creatorFamilyName = settings.getString(CFG_CREATOR_FAMILY_NAME);
		creatorContact = settings.getString(CFG_CREATOR_CONTACT);
		createdDate = settings.getLong(CFG_CREATION_DATE);
		modifiedDate = settings.getLong(CFG_MODIFICATION_DATE);
		isSecondary = settings.getBoolean(CFG_IS_SECONDARY);
		overwrite = settings.getBoolean(CFG_OVERWRITE);
		splitModels = settings.getBoolean(CFG_SPLIT_MODELS);
		referenceDescriptionLink = settings.getString(CFG_REFERENCE_LINK);
		license = settings.getString(CFG_LICENSE);
		notes = settings.getString(CFG_NOTES);
	  }

	  void save(final NodeSettingsWO settings) {
	    settings.addString(CFG_FILE, filePath);
	    settings.addString(CFG_OUT_PATH, outPath);
		settings.addString(CFG_MODEL_NAME, modelName);
		settings.addString(CFG_CREATOR_GIVEN_NAME, creatorGivenName);
		settings.addString(CFG_CREATOR_FAMILY_NAME, creatorFamilyName);
		settings.addString(CFG_CREATOR_CONTACT, creatorContact);
		settings.addLong(CFG_CREATION_DATE, createdDate);
		settings.addLong(CFG_MODIFICATION_DATE, modifiedDate);
		settings.addBoolean(CFG_IS_SECONDARY, isSecondary);
		settings.addBoolean(CFG_OVERWRITE, overwrite);
		settings.addBoolean(CFG_SPLIT_MODELS, splitModels);
		settings.addString(CFG_REFERENCE_LINK, referenceDescriptionLink);
		settings.addString(CFG_LICENSE, license);
		settings.addString(CFG_NOTES, notes);
	  }
	  
	  
	  
	  private static final String CFG_OUT_PATH = "outPath";
		private static final String CFG_MODEL_NAME = "modelName";
		private static final String CFG_CREATOR_GIVEN_NAME = "CreatorGivenName";
		private static final String CFG_CREATOR_FAMILY_NAME = "CreatorFamilyName";
		private static final String CFG_CREATOR_CONTACT = "CreatorContact";
		private static final String CFG_CREATION_DATE = "CreationDateVALUEKEYSETTINGS";
		private static final String CFG_MODIFICATION_DATE = "ModifiedDateVALUEKEYSETTINGS";
		private static final String CFG_IS_SECONDARY = "isSecondary";
		private static final String CFG_OVERWRITE = "overwrite";
		private static final String CFG_SPLIT_MODELS = "splitModels";
		private static final String CFG_REFERENCE_LINK = "referenceLink";
		private static final String CFG_LICENSE = "license";
		private static final String CFG_NOTES = "notes";

		public String outPath = "";
		public String modelName = "";
		public String creatorGivenName = "";
		public String creatorFamilyName = "";
		public String creatorContact = "";

		/** Creation date in milliseconds since 1970 in UTC time. */
		public long createdDate;

		/** Last modification date in milliseconds since 1970 in UTC time. */
		public long modifiedDate;

		public boolean isSecondary = false;
		public boolean overwrite = false;
		public boolean splitModels = false;
		public String referenceDescriptionLink = "";
		public String license = "";
		public String notes = "";

	
}
