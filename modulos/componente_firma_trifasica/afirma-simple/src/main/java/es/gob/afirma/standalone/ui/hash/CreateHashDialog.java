package es.gob.afirma.standalone.ui.hash;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import es.gob.afirma.core.AOCancelledOperationException;
import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.Platform;
import es.gob.afirma.core.ui.AOUIFactory;
import es.gob.afirma.standalone.AutoFirmaUtil;
import es.gob.afirma.standalone.SimpleAfirmaMessages;
import es.gob.afirma.standalone.ui.preferences.PreferencesManager;

/** Di&aacute;logo para la creaci&oacute;n de huellas digitales.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s */
public final class CreateHashDialog extends JDialog implements KeyListener{

	private static final long serialVersionUID = 3581001930027153381L;

	private static final String PREFERENCE_BASE64 = "createHashAsBase64"; //$NON-NLS-1$
	private static final String PREFERENCE_ALGORITHM = "createHashAlgorithm"; //$NON-NLS-1$

	private static final String[] HASH_ALGOS = new String[] {
		"SHA-1", //$NON-NLS-1$
		"SHA-256", //$NON-NLS-1$
		"SHA-384", //$NON-NLS-1$
		"SHA-512" //$NON-NLS-1$
	};

	private final JComboBox<String> hashAlgorithms = new JComboBox<>(HASH_ALGOS);
	String getSelectedHashAlgorithm() {
		return this.hashAlgorithms.getSelectedItem().toString();
	}

	private final JTextField fileTextField = new JTextField();
	JTextField getFileTextField() {
		return this.fileTextField;
	}

	private final JCheckBox base64ChechBox = new JCheckBox(
		SimpleAfirmaMessages.getString("CreateHashDialog.0") //$NON-NLS-1$
	);
	boolean isBase64Checked() {
		return this.base64ChechBox.isSelected();
	}

	/** Inicia el proceso de creaci&oacute;n de huella digital.
	 * @param parent Componente padre para la modalidad. */
	public static void startHashCreation(final Frame parent) {
		new CreateHashDialog(parent).setVisible(true);
	}

	/** Crea un di&aacute;logo para la creaci&oacute;n de huellas digitales.
	 * @param parent Componente padre para la modalidad. */
	private CreateHashDialog(final Frame parent) {
		super(parent);
		setTitle(SimpleAfirmaMessages.getString("CreateHashDialog.15")); //$NON-NLS-1$
		setModalityType(ModalityType.APPLICATION_MODAL);
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					createUI(parent);
				}
			}
		);
	}

	void createUI(final Frame parent) {

		final Container c = getContentPane();
		final GridBagLayout gbl = new GridBagLayout();
		c.setLayout(gbl);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,15,0,10);
		setIconImage(
			AutoFirmaUtil.getDefaultDialogsIcon()
		);
		getAccessibleContext().setAccessibleDescription(
			SimpleAfirmaMessages.getString("CreateHashDialog.1") //$NON-NLS-1$
		);

		final JLabel hashAlgorithmsLabels = new JLabel(
			SimpleAfirmaMessages.getString("CreateHashDialog.2") //$NON-NLS-1$
		);
		hashAlgorithmsLabels.addKeyListener(this);
		hashAlgorithmsLabels.setLabelFor(this.hashAlgorithms);

		this.hashAlgorithms.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					PreferencesManager.put(
						PREFERENCE_ALGORITHM,
						getSelectedHashAlgorithm()
					);
				}
			}
		);
		this.hashAlgorithms.setSelectedItem(
			PreferencesManager.get(
				PREFERENCE_ALGORITHM,
				"SHA-512" //$NON-NLS-1$
			)
		);
		this.hashAlgorithms.addKeyListener(this);

		this.base64ChechBox.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					PreferencesManager.putBoolean(
						PREFERENCE_BASE64,
						isBase64Checked()
					);
				}
			}
		);
		this.base64ChechBox.setSelected(
			PreferencesManager.getBoolean(
				PREFERENCE_BASE64,
				false
			)
		);
		this.base64ChechBox.addKeyListener(this);

		final JLabel fileTextFieldLabel = new JLabel(
			SimpleAfirmaMessages.getString("CreateHashDialog.3") //$NON-NLS-1$
		);
		fileTextFieldLabel.addKeyListener(this);
		fileTextFieldLabel.setLabelFor(this.fileTextField);
		this.fileTextField.addKeyListener(this);
		this.fileTextField.setEditable(false);
		this.fileTextField.setFocusable(false);
		this.fileTextField.setColumns(10);

		final JButton generateButton = new JButton(
			SimpleAfirmaMessages.getString("CreateHashDialog.4") //$NON-NLS-1$
		);
		generateButton.addKeyListener(this);

		final JButton fileButton = new JButton(
			SimpleAfirmaMessages.getString("CreateHashDialog.5") //$NON-NLS-1$
		);
		fileButton.addKeyListener(this);
		fileButton.setMnemonic('E');
		fileButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ae) {
					try {
						getFileTextField().setText(
							AOUIFactory.getLoadFiles(
								SimpleAfirmaMessages.getString("CreateHashDialog.6"), //$NON-NLS-1$,
								null,
								null,
								null,
								SimpleAfirmaMessages.getString("CreateHashDialog.7"), //$NON-NLS-1$,,
								false,
								false,
								AutoFirmaUtil.getDefaultDialogsIcon(),
								CreateHashDialog.this
							)[0].getAbsolutePath()
						);
						generateButton.setEnabled(true);
					}
					catch(final AOCancelledOperationException ex) {
						// Operacion cancelada por el usuario
					}
				}
			}
		);
		fileButton.getAccessibleContext().setAccessibleDescription(
			SimpleAfirmaMessages.getString("CreateHashDialog.12") //$NON-NLS-1$
		);
		fileButton.addKeyListener(this);

		generateButton.setEnabled(false);
		generateButton.setMnemonic('G');
		generateButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					doHashProcess(
						parent,
						getFileTextField().getText(),
						getSelectedHashAlgorithm(),
						isBase64Checked(),
						CreateHashDialog.this
					);
					CreateHashDialog.this.setVisible(false);
					CreateHashDialog.this.dispose();
				}
			}
		);
		generateButton.getAccessibleContext().setAccessibleDescription(
			SimpleAfirmaMessages.getString("CreateHashDialog.11") //$NON-NLS-1$
		);

		final JButton exitButton = new JButton(
			SimpleAfirmaMessages.getString("CreateHashDialog.16") //$NON-NLS-1$
		);

		exitButton.setMnemonic('C');
		exitButton.addActionListener( new ActionListener () {
			@Override
			public void actionPerformed( final ActionEvent e ) {
				CreateHashDialog.this.setVisible(false);
				CreateHashDialog.this.dispose();
			}
		});
		exitButton.getAccessibleContext().setAccessibleDescription(
			SimpleAfirmaMessages.getString("CreateHashDialog.17") //$NON-NLS-1$
		);
		exitButton.addKeyListener(this);

		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		// En Mac OS X el orden de los botones es distinto
		if (Platform.OS.MACOSX.equals(Platform.getOS())) {
			panel.add(generateButton);
			panel.add(exitButton);
		}
		else {
			panel.add(exitButton);
			panel.add(generateButton);
		}

		c.add(fileTextFieldLabel, gbc);
		gbc.insets = new Insets(5,10,0,10);
		gbc.gridy++;
		c.add(this.fileTextField, gbc);
		gbc.weightx = 0;
		c.add(fileButton, gbc);
		gbc.insets = new Insets(25,15,0,10);
		gbc.weightx = 1.0;
		gbc.gridy++;
		c.add(hashAlgorithmsLabels, gbc);
		gbc.insets = new Insets(5,10,0,10);
		gbc.gridy++;
		c.add(this.hashAlgorithms, gbc);
		gbc.insets = new Insets(25,10,0,10);
		gbc.gridy++;
		c.add(this.base64ChechBox, gbc);
		gbc.insets = new Insets(20,10,0,10);
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		c.add(panel, gbc);
		pack();
		setSize(600, 280);
		setResizable(false);
		setLocationRelativeTo(parent);

	}

	static void doHashProcess(final Frame parent,
                              final String file,
                              final String hashAlgorithm,
                              final boolean base64,
                              final Window currentFrame) {

		try ( final InputStream is = new FileInputStream(file); ) {

			if (currentFrame != null) {
				currentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}

			final byte[] data = AOUtil.getDataFromInputStream(is);
			final byte[] hash = MessageDigest.getInstance(
				hashAlgorithm
			).digest(data);

			final String ext = base64 ? ".hashb64" : ".hash"; //$NON-NLS-1$ //$NON-NLS-2$

			AOUIFactory.getSaveDataToFile(
					base64 ? Base64.encode(hash).getBytes() :
						hash,
				SimpleAfirmaMessages.getString("CreateHashDialog.8"), //$NON-NLS-1$,,,
				null,
				new java.io.File(file).getName() + ext,
				new String[] { ext },
				SimpleAfirmaMessages.getString("CreateHashDialog.9") + " (*" + ext + ")",  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				parent
			);
		}
		catch(final OutOfMemoryError ooe) {
			AOUIFactory.showErrorMessage(
				parent,
				SimpleAfirmaMessages.getString("CreateHashDialog.18"), //$NON-NLS-1$
				SimpleAfirmaMessages.getString("CreateHashDialog.14"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE
			);
			Logger.getLogger("es.gob.afirma").severe( //$NON-NLS-1$
				"Fichero demasiado grande: " + ooe //$NON-NLS-1$
			);
			return;
		}
		catch(final AOCancelledOperationException aocoe) {
			return;
		}
		catch (final Exception ioe) {
			AOUIFactory.showErrorMessage(
				parent,
				SimpleAfirmaMessages.getString("CreateHashDialog.13"), //$NON-NLS-1$
				SimpleAfirmaMessages.getString("CreateHashDialog.14"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE
			);
			Logger.getLogger("es.gob.afirma").severe( //$NON-NLS-1$
				"Error generando o guardando la huella digital: " + ioe //$NON-NLS-1$
			);
			return;
		}
		finally {
			if (currentFrame != null) {
				currentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

	}

	/** {@inheritDoc} */
	@Override
	public void keyTyped(final KeyEvent e) { /* Vacio */ }

	/** {@inheritDoc} */
	@Override
	public void keyPressed(final KeyEvent e) { /* Vacio */ }

	/** {@inheritDoc} */
	@Override
	public void keyReleased(final KeyEvent ke) {
		// En Mac no cerramos los dialogos con Escape
		if (ke != null && ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			CreateHashDialog.this.setVisible(false);
			CreateHashDialog.this.dispose();
		}
	}

}
