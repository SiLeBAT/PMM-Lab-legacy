package de.bund.bfr.knime.pmm.common.units;

import java.util.concurrent.Callable;

public class MyUnitCaller implements Callable<Void> {
	  @Override
	  public Void call() {
		  CategoryReader.killInstance();
		  return null;
	  }
	} 