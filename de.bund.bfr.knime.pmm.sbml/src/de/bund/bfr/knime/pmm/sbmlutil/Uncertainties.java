package de.bund.bfr.knime.pmm.sbmlutil;

import de.bund.bfr.knime.pmm.common.EstModelXml;

public class Uncertainties {

	EstModelXml estModel;

	public Uncertainties(EstModelXml estModel) {
		this.estModel = estModel;
	}

	/**
	 * Inits uncertainties
	 */
	public Uncertainties(int id, String name, String comment, Double r2, Double rms, Double sse, Double aic, Double bic,
			Integer dof) {
		estModel = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof);
		estModel.setComment(comment);
		estModel.setQualityScore(0); // unchecked model
	}

	public EstModelXml getEstModelXml() {
		return estModel;
	}

	@Override
	public boolean equals(Object obj) {
		EstModelXml otherEstModel = ((Uncertainties) obj).getEstModelXml();

		if (estModel.getId() != otherEstModel.getId())
			return false;
		if (!estModel.getComment().equals(otherEstModel.getComment()))
			return false;
		if (Double.compare(estModel.getR2(), otherEstModel.getR2()) != 0)
			return false;
		if (Double.compare(estModel.getRms(), otherEstModel.getRms()) != 0)
			return false;
		if (Double.compare(estModel.getSse(), otherEstModel.getSse()) != 0)
			return false;
		if (Double.compare(estModel.getAic(), otherEstModel.getAic()) != 0)
			return false;
		if (Double.compare(estModel.getBic(), otherEstModel.getBic()) != 0)
			return false;
		if (estModel.getDof() != otherEstModel.getDof())
			return false;

		return true;
	}
}
