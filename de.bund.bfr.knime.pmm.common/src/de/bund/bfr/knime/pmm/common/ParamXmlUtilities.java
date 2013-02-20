package de.bund.bfr.knime.pmm.common;

public class ParamXmlUtilities {

	public static boolean isOutOfRange(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			ParamXml element = (ParamXml) el;

			if (element.getValue() != null) {
				if (element.getMin() != null
						&& element.getValue() < element.getMin()) {
					return true;
				}

				if (element.getMax() != null
						&& element.getValue() > element.getMax()) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean covarianceMatrixMissing(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			if (((ParamXml) el).getError() == null) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNotSignificant(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			Double p = ((ParamXml) el).getP();

			if (p != null && p > 0.95) {
				return true;
			}
		}

		return false;
	}

}
