/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 *
 * History
 *   02.04.2012 (hofer): created
 */
package fsk.template;

import fsk.RSnippetTemplate;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.NodeLogger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;

/**
 * A central provider for Java Snippet templates.
 *
 * @author Heiko Hofer.
 */
@SuppressWarnings("rawtypes")
public class TemplateProvider extends TemplateRepository implements ChangeListener {

    private static final String EXTENSION_POINT_ID = "fsk.templaterepository";
    private static final Object LOCK = new Object[0];
    private static final NodeLogger LOGGER = NodeLogger.getLogger(TemplateProvider.class);

    /**
     * The display name of the category with all templates.
     */
    static final String ALL_CATEGORY = "All";

    private Map<Class, Map<String, Collection<RSnippetTemplate>>> m_templates;
    private List<TemplateRepository> m_repos;
    private FileTemplateRepository m_defaultRepo;

    private static TemplateProvider provider;

    /**
     * prevent instantiation from outside.
     */
    private TemplateProvider() {
        m_defaultRepo =
                (FileTemplateRepository) new DefaultFileTemplateRepositoryProvider().getRepository();
        m_repos = new ArrayList<>();
        IConfigurationElement[] config =
                Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);

        for (IConfigurationElement e : config) {
            try {
                final Object o = e.createExecutableExtension("provider-class");
                if (o instanceof TemplateRepositoryProvider) {
                    TemplateRepository repo = ((TemplateRepositoryProvider) o).getRepository();
                    if (null != repo) {
                        repo.addChangeListener(this);
                        m_repos.add(repo);
                    }
                }
            } catch (CoreException ex) {
                LOGGER.error("Error while reading rsnippet template " + "repositories.", ex);
            }
        }
        m_templates = new HashMap<>();
    }

    /**
     * Get default shared instance.
     *
     * @return default TemplateProvider.
     */
    public static TemplateProvider getDefault() {
        synchronized (LOCK) {
            if (provider == null) {
                provider = new TemplateProvider();
            }
        }

        return provider;
    }

    /**
     * Get all categories.
     *
     * @param m_metaCategories only categories that hold templates in this meta categories will be
     *                         displayed.
     * @return the categories.
     */
    Set<String> getCategories(final Collection<Class<?>> m_metaCategories) {
        initTemplates(m_metaCategories);
        Set<String> categories = new LinkedHashSet<>();
        m_metaCategories.stream().filter(c -> m_templates.containsKey(c)).forEach(
                c -> categories.addAll(m_templates.get(c).keySet()));

        return categories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<RSnippetTemplate> getTemplates(Collection<Class<?>> metaCategories) {
        initTemplates(metaCategories);
        Set<RSnippetTemplate> templates = new LinkedHashSet<>();
        metaCategories.stream().filter(c -> m_templates.containsKey(c)).forEach(
                c -> templates.addAll(m_templates.get(c).get(ALL_CATEGORY)));

        return templates;
    }

    /**
     * Get the {@link RSnippetTemplate} in the given category.
     *
     * @param metaCategories only templates from these meta categories will be returned.
     * @param category       a category as given by getCategories()
     * @return the {@link RSnippetTemplate}s in the given category.
     */
    Collection<RSnippetTemplate> getTemplates(final Collection<Class<?>> metaCategories, final String category) {
        initTemplates(metaCategories);
        Set<RSnippetTemplate> templates = new LinkedHashSet<>();
        metaCategories.stream().filter(c -> m_templates.containsKey(c)).forEach(c -> templates.addAll(m_templates.get
                (c).get(category)));

        return templates;
    }

    /**
     * Load templates for the given meta categories.
     *
     * @param m_metaCategories the meta categories.
     */
    private void initTemplates(final Collection<Class<?>> m_metaCategories) {
        // reset data
        for (Class key : m_metaCategories) {
            if (m_templates.containsKey(key)) {
                m_templates.remove(key);
            }
            Map<String, Collection<RSnippetTemplate>> templates = new LinkedHashMap<>();
            templates.put(ALL_CATEGORY, new ArrayList<>());
            m_templates.put(key, templates);
        }
        m_repos.forEach(repo -> appendTemplates(repo.getTemplates(m_metaCategories)));
    }

    /**
     * Add a template to the default location.
     *
     * @param template the template.
     */
    public void addTemplate(final RSnippetTemplate template) {
        m_defaultRepo.removeChangeListener(this);
        m_defaultRepo.addTemplate(template);
        m_defaultRepo.addChangeListener(this);
        appendTemplates(Collections.singletonList(template));
        // notify listeners
        fireStateChanged();
    }

    /**
     * Append to given list of templates.
     *
     * @param templates the templates.
     */
    private void appendTemplates(final Collection<RSnippetTemplate> templates) {
        if (templates == null) {
            return;
        }
        templates.forEach(t -> appendTemplatesTo(m_templates.get(t.getMetaCategory()), t));
    }

    /**
     * Append the template to the given map.
     *
     * @param map      the map.
     * @param template the template
     */
    private void appendTemplatesTo(final Map<String, Collection<RSnippetTemplate>> map, final RSnippetTemplate
            template) {
        map.get(ALL_CATEGORY).add(template);
        String key = template.getCategory();
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(template);
    }

    /**
     * Test if a template can be removed.
     *
     * @param template the template.
     * @return true when removeTemplate(template) could be successful.
     */
    @Override
    public boolean isRemoveable(RSnippetTemplate template) {
        return m_repos.stream().anyMatch(repo -> repo.isRemoveable(template));
    }

    /**
     * Remove the given template.
     *
     * @param template the template to be removed.
     * @return when the template is successfully removed.
     */
    @Override
    public boolean removeTemplate(RSnippetTemplate template) {
        if (isRemoveable(template)) {
            boolean success;
            for (TemplateRepository repo : m_repos) {
                repo.removeChangeListener(this);
                success = repo.removeTemplate(template);
                if (success) {
                    break;
                }
            }
            fireStateChanged();
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        // A template changed unexpectedly, reset temporary data
        m_templates.clear();
        // notify listeners
        fireStateChanged();
    }

    /**
     * Get the template with the given id.
     *
     * @param id the id
     * @return the template or null if a template with the id does not exist.
     * @throws NullPointerException if id is null.
     */
    @Override
    public RSnippetTemplate getTemplate(UUID id) {
        if (id == null) {
            throw new NullPointerException("UUID is null.");
        }
        for (TemplateRepository repo : m_repos) {
            RSnippetTemplate template = repo.getTemplate(id);
            if (template != null) {
                return template;
            }
        }

        return null;
    }

    /**
     * Get a short descriptive string about the location of the template. This should give the user
     * an idea where the template comes from. It can be a path to a file, or the name of a company
     * with a template name like "Fibonacci (KNIME)" for a template from KNIME that generates the
     * Fibonacci numbers.
     *
     * @param template the template
     * @return the string describing the location of the template or null if no string could be
     * generated.
     * @throws NullPointerException if template if null.
     */
    @Override
    public String getDisplayLocation(RSnippetTemplate template) {
        if (template == null) {
            throw new NullPointerException("template is null");
        }
        for (TemplateRepository repo :
                m_repos) {
            String loc = repo.getDisplayLocation(template);
            if (loc != null) {
                return loc;
            }
        }

        return null;
    }
}
