package es.gob.afirma.standalone.ui.preferences;

import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_ACCEPTED_POLICIES_ONLY_CERTS;
import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_ALIAS_ONLY_CERTS;
import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_CYPH_ONLY_CERTS;
import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_DEFAULT_STORE;
import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_PRIORITARY_STORE;
import static es.gob.afirma.standalone.ui.preferences.PreferencesManager.PREFERENCE_KEYSTORE_SIGN_ONLY_CERTS;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.gob.afirma.core.misc.Platform;
import es.gob.afirma.core.ui.AOUIFactory;
import es.gob.afirma.keystores.AOKeyStore;
import es.gob.afirma.keystores.AOKeyStoreDialog;
import es.gob.afirma.keystores.AOKeyStoreManager;
import es.gob.afirma.keystores.AOKeyStoreManagerFactory;
import es.gob.afirma.standalone.SimpleAfirmaMessages;
import es.gob.afirma.standalone.SimpleKeyStoreManager;
import es.gob.afirma.ui.core.jse.certificateselection.CertificateSelectionDialog;

final class PreferencesPanelKeyStores extends JPanel {

	private static final long serialVersionUID = 3255071607793273334L;

	private final JCheckBox onlySignature = new JCheckBox(
		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.0"), //$NON-NLS-1$
		PreferencesManager.getBoolean(PREFERENCE_KEYSTORE_SIGN_ONLY_CERTS, false)
	);

	private final JCheckBox onlyEncipherment = new JCheckBox(
		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.1"), //$NON-NLS-1$
		PreferencesManager.getBoolean(PREFERENCE_KEYSTORE_CYPH_ONLY_CERTS, false)
	);

	private final JCheckBox onlyAlias = new JCheckBox(
		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.4"), //$NON-NLS-1$
		PreferencesManager.getBoolean(PREFERENCE_KEYSTORE_ALIAS_ONLY_CERTS, false)
	);

	private final JComboBox<String> prioritaryKeyStoreComboBox = new JComboBox<>(
		new String[] {
			SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.20"), // Ninguno //$NON-NLS-1$
			AOKeyStore.DNIEJAVA.toString(),
			AOKeyStore.CERES.toString()
		}
	);

	private static AOKeyStore[] DEFAULT_STORES;
	static {
		final List<AOKeyStore> stores = new ArrayList<>();
		final Platform.OS os = Platform.getOS();
		if (Platform.OS.WINDOWS.equals(os)) {
			stores.add(AOKeyStore.WINDOWS);
		}
		else if (Platform.OS.MACOSX.equals(os)) {
			stores.add(AOKeyStore.APPLE);
		}
		else {
			stores.add(AOKeyStore.SHARED_NSS);
		}
		if (SimpleKeyStoreManager.isFirefoxAvailable()) {
			stores.add(AOKeyStore.MOZ_UNI);
		}
		DEFAULT_STORES = stores.toArray(new AOKeyStore[0]);
	}

	private final JComboBox<AOKeyStore> defaultStore = new JComboBox<>(DEFAULT_STORES);
	AOKeyStore getDefaultStore() {
		return this.defaultStore.getItemAt(this.defaultStore.getSelectedIndex());
	}

	private final JButton contentButton = new JButton(
		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.9") //$NON-NLS-1$
	);

	JButton getContentButton() {
		return this.contentButton;
	}

	PreferencesPanelKeyStores(final KeyListener keyListener,
							  final ModificationListener modificationListener,
							  final boolean unprotected) {

		createUI(keyListener, modificationListener, unprotected);
	}

	void createUI(final KeyListener keyListener,
				  final ModificationListener modificationListener,
				  final boolean unprotected) {

        setLayout(new GridBagLayout());

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridy = 0;

        final JPanel keysFilerPanel = new JPanel(new GridBagLayout());
        keysFilerPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createTitledBorder(SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.6")) //$NON-NLS-1$
			)
		);

		final GridBagConstraints kfc = new GridBagConstraints();
		kfc.fill = GridBagConstraints.HORIZONTAL;
		kfc.weightx = 1.0;
		kfc.gridy = 0;
		kfc.insets = new Insets(5, 7, 5, 7);

	    this.onlySignature.getAccessibleContext().setAccessibleDescription(
    		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.2") //$NON-NLS-1$
		);
	    this.onlySignature.setMnemonic('i');
	    this.onlySignature.addItemListener(modificationListener);
	    this.onlySignature.addKeyListener(keyListener);
	    this.onlySignature.setEnabled(unprotected);
	    keysFilerPanel.add(this.onlySignature, kfc);

        kfc.gridy++;

	    this.onlyEncipherment.getAccessibleContext().setAccessibleDescription(
    		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.3") //$NON-NLS-1$
		);
	    this.onlyEncipherment.setMnemonic('r');
	    this.onlyEncipherment.addItemListener(modificationListener);
	    this.onlyEncipherment.addKeyListener(keyListener);
	    this.onlyEncipherment.setEnabled(unprotected);

        //TODO: Descomentar una vez se entregue
	    //keysFilerPanel.add(this.onlyEncipherment, kfc);

        kfc.gridy++;

	    this.onlyAlias.getAccessibleContext().setAccessibleDescription(
    		SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.5") //$NON-NLS-1$
		);
	    this.onlyAlias.setMnemonic('s');
	    this.onlyAlias.addItemListener(modificationListener);
	    this.onlyAlias.addKeyListener(keyListener);
	    this.onlyAlias.setEnabled(unprotected);
	    keysFilerPanel.add(this.onlyAlias, kfc);

	    final JPanel trustPanel = new JPanel();
	    trustPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

	    final JButton configureCertPoliciesButton = new JButton(SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.13")); //$NON-NLS-1$
	    configureCertPoliciesButton.setEnabled(PreferencesManager.getBoolean(PREFERENCE_KEYSTORE_ACCEPTED_POLICIES_ONLY_CERTS, false));
		configureCertPoliciesButton.setMnemonic('F');
		configureCertPoliciesButton.getAccessibleContext().setAccessibleDescription(
				SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.14") //$NON-NLS-1$
		);
		configureCertPoliciesButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					CertPoliciesDialog.startCertPoliciesDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, getParent()));
				}
			}
		);
		configureCertPoliciesButton.addKeyListener(keyListener);
		configureCertPoliciesButton.setEnabled(unprotected);

		final JLabel certPoliciesLabel = new JLabel(
				SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.12") //$NON-NLS-1$
		);
		certPoliciesLabel.addKeyListener(keyListener);
		certPoliciesLabel.setLabelFor(configureCertPoliciesButton);

		trustPanel.add(certPoliciesLabel);
	    trustPanel.add(configureCertPoliciesButton);

	    kfc.gridy++;
	    kfc.insets = new Insets(0,2,0,0);
	    keysFilerPanel.add(trustPanel, kfc);
	    kfc.insets = new Insets(5, 7, 5, 7);

        final JPanel priorityKeysStorePanel = new JPanel(new GridBagLayout());
        priorityKeysStorePanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createTitledBorder(SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.18")) //$NON-NLS-1$
			)
		);

		final GridBagConstraints pksc = new GridBagConstraints();
		pksc.anchor = GridBagConstraints.LINE_START;
		pksc.weightx = 1.0;
		pksc.gridy = 0;
		pksc.fill = GridBagConstraints.HORIZONTAL;
		pksc.insets = new Insets(5, 7, 5, 7);

		final JLabel cpriorityKeysStoreLabel = new JLabel(
				SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.19") //$NON-NLS-1$
		);
		cpriorityKeysStoreLabel.addKeyListener(keyListener);
		cpriorityKeysStoreLabel.setLabelFor(this.prioritaryKeyStoreComboBox);

		this.prioritaryKeyStoreComboBox.addItemListener(modificationListener);
		this.prioritaryKeyStoreComboBox.addKeyListener(keyListener);

		priorityKeysStorePanel.add(cpriorityKeysStoreLabel, pksc);
		pksc.gridy++;
		pksc.fill = GridBagConstraints.NONE;
		priorityKeysStorePanel.add(this.prioritaryKeyStoreComboBox, pksc);

        final JPanel keysStorePanel = new JPanel(new GridBagLayout());
        keysStorePanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createTitledBorder(SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.7")) //$NON-NLS-1$
			)
		);

		final GridBagConstraints ksc = new GridBagConstraints();
		ksc.anchor = GridBagConstraints.LINE_START;
		ksc.gridy = 0;
		ksc.insets = new Insets(5, 7, 5, 7);

		this.defaultStore.setSelectedItem(
			SimpleKeyStoreManager.getDefaultKeyStoreType()
		);
		this.defaultStore.addItemListener(
			new ItemListener() {
				@Override
				public void itemStateChanged(final ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						SwingUtilities.invokeLater(
							new Runnable() {
								@Override
								public void run() {
									AOUIFactory.showMessageDialog(
										PreferencesPanelKeyStores.this,
										SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.16"), //$NON-NLS-1$
										SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.17"), //$NON-NLS-1$
										JOptionPane.WARNING_MESSAGE
									);
								}
							}
						);
					}
				}
			}
		);

		this.defaultStore.addItemListener(modificationListener);
		this.defaultStore.addKeyListener(keyListener);

		//TODO: Descomentar una vez se entregue
		keysStorePanel.add(this.defaultStore, ksc);

		this.contentButton.setMnemonic('V');
		this.contentButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ae) {
					final AOKeyStoreManager ksm;
					try {
						ksm = AOKeyStoreManagerFactory.getAOKeyStoreManager(
							getDefaultStore(),
							null,
							"default", //$NON-NLS-1$
							getDefaultStore().getStorePasswordCallback(this),
							this
						);

						final CertificateSelectionDialog csd = new CertificateSelectionDialog(
							PreferencesPanelKeyStores.this,
							new AOKeyStoreDialog(
								ksm,
								this,
								true,
								true,
								false
							),
							SimpleAfirmaMessages.getString(
								"PreferencesPanelKeyStores.10", //$NON-NLS-1$
								getDefaultStore().toString()
							),
							SimpleAfirmaMessages.getString(
								"PreferencesPanelKeyStores.15", //$NON-NLS-1$
								getDefaultStore().toString()
							),
							false,
							true
						);
						csd.showDialog();
					}
					catch (final Exception e) {
						AOUIFactory.showErrorMessage(
							this,
							SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.11"), //$NON-NLS-1$
							SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.10", getDefaultStore().toString()), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE
						);
						Logger.getLogger("es.gob.afirma").warning("Error al recuperar el almacen por defecto seleccionado: " + e); //$NON-NLS-1$ //$NON-NLS-2$
					}

				}
			}
		);
		this.contentButton.getAccessibleContext().setAccessibleDescription(
			SimpleAfirmaMessages.getString("PreferencesPanelKeyStores.8") //$NON-NLS-1$
		);
		this.contentButton.addKeyListener(keyListener);

		//TODO: Descomentar una vez se entregue
		keysStorePanel.add(this.contentButton, ksc);

		ksc.weightx = 1.0;
		keysStorePanel.add(new JPanel(), ksc);

	    add(keysFilerPanel, c);
	    c.gridy++;
	    add(priorityKeysStorePanel, c);
	    c.gridy++;

	  //TODO: Descomentar una vez se entregue
	    add(keysStorePanel, c);

	    c.weighty = 1.0;
	    c.gridy++;
		add(new JPanel(), c);
	}

	void savePreferences() {
		PreferencesManager.putBoolean(PREFERENCE_KEYSTORE_SIGN_ONLY_CERTS, this.onlySignature.isSelected());
		PreferencesManager.putBoolean(PREFERENCE_KEYSTORE_CYPH_ONLY_CERTS, this.onlyEncipherment.isSelected());
		PreferencesManager.putBoolean(PREFERENCE_KEYSTORE_ALIAS_ONLY_CERTS, this.onlyAlias.isSelected());
		PreferencesManager.put(
			PREFERENCE_KEYSTORE_DEFAULT_STORE,
			getDefaultStore().toString()
		);
		PreferencesManager.put(
			PREFERENCE_KEYSTORE_PRIORITARY_STORE,
			this.prioritaryKeyStoreComboBox.getSelectedItem().toString()
		);
	}
}
