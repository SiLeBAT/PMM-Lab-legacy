package de.bund.bfr.knime.pmm.jsonencoder;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;

public class TimeSeriesJsonEncoder {

	JsonIntEncoder condIdCoder;
	JsonStringEncoder combaseIdCoder;
	JsonAgentEncoder organismCoder;
	
	private JSONObject obj;
	
	/**
	 * Initialize encoder
	 * @param condId
	 * @param combaseID
	 * @param organism
	 * @param matrix
	 * @param mdData
	 * @param misc
	 * @param mdInfo
	 * @param mdLit
	 * @param mdDBUID
	 */
	public TimeSeriesJsonEncoder(Integer condId, String combaseId,
			AgentXml organism, PmmXmlDoc matrix, PmmXmlDoc mdData,
			PmmXmlDoc misc, PmmXmlDoc mdInfo, PmmXmlDoc mdLit, String mdDBUID) {

		condIdCoder = new JsonIntEncoder("CondID", condId);
		combaseIdCoder = new JsonStringEncoder("CombaseId", combaseId);
		organismCoder = new JsonAgentEncoder("Organism", organism);
		
		// TODO: ...
		obj = new JSONObject();
//		obj.put(key, value)
	}
	
	public JSONObject getObj() {
		return obj;
	}
}
