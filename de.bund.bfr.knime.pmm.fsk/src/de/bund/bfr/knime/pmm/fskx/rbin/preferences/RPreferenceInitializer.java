/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.pmm.fskx.rbin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.bund.bfr.knime.pmm.fskx.FSKNodePlugin;

/**
 * Initializes preference page with default paths to R2 and R3 environments.
 *
 * @author Miguel Alba
 */
public class RPreferenceInitializer extends AbstractPreferenceInitializer {

  /** Path to R v.2 */
  public static final String R2_PATH = "r2.path";

  /** Path to R v.3 */
  public static final String R3_PATH = "r3.path";

  private static RPreferenceProvider m_cachedR2PreferenceProvider = null;
  private static RPreferenceProvider m_cachedR3PreferenceProvider = null;

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = FSKNodePlugin.getDefault().getPreferenceStore();
    store.setDefault(R2_PATH, "");
    store.setDefault(R3_PATH, "");
  }

  /** @return provider to the path to the R2 executable. */
  public static final RPreferenceProvider getR2Provider() {
    final String r2Home = FSKNodePlugin.getDefault().getPreferenceStore().getString(R2_PATH);
    if (m_cachedR2PreferenceProvider == null
        || !m_cachedR2PreferenceProvider.getRHome().equals(r2Home)) {
      m_cachedR2PreferenceProvider = new DefaultRPreferenceProvider(r2Home);
    }

    return m_cachedR2PreferenceProvider;
  }

  /** @return provider to the path to the R3 executable. */
  public static final RPreferenceProvider getR3Provider() {
    final String r3Home = FSKNodePlugin.getDefault().getPreferenceStore().getString(R3_PATH);
    if (m_cachedR3PreferenceProvider == null
        || !m_cachedR3PreferenceProvider.getRHome().equals(r3Home)) {
      m_cachedR3PreferenceProvider = new DefaultRPreferenceProvider(r3Home);
    }

    return m_cachedR3PreferenceProvider;
  }

  /**
   * Invalidate the cached R2 preference provider return by {@link #getR2Provider()}, to refetch R
   * properties (which launches an external R command).
   */
  public static final void invalidateR2PreferenceProviderCache() {
    m_cachedR2PreferenceProvider = null;
  }

  /**
   * Invalidate the cached R3 preference provider returned by {@link #getR3Provider()}, to refetch R
   * properties (which launches an external R command).
   */
  public static final void invalidateR3PreferenceProviderCache() {
    m_cachedR3PreferenceProvider = null;
  }
}
