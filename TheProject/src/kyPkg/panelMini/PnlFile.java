package kyPkg.panelMini;

import kyPkg.etc.UserAgent;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ParamMapUtil;
import kyPkg.util.*;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.*;
import java.awt.event.*; //import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;

public class PnlFile extends JPanel {

    public static final String CAPTION = "caption";
    public static final String OPT49ER = "49er";
    public static final String ENCODE = "Encode";
    public static final String DELIM = "Delim";
    public static final String DEFAULT_PATH = "DefaultPath";
    public static final String CAPTION_WIDTH = "CaptionWidth";
    public static final String USER_AGENT = "UserAgent";
    //-------------------------------------------------------------------------
    private static final String COMMNA = "Commna";
    private static final String FSP = System.getProperty("file.separator");
    private static final long serialVersionUID = 495164841086015812L;
    private String currentPath = "";
    //-------------------------------------------------------------------------
    private String caption = "file:";
    private boolean opt49er = true;
    private boolean optEncode = false;
    private boolean optDelim = false;
    private String defaultPath;
    private int captionWidth = -1;
    private boolean useragent = false;
    //-------------------------------------------------------------------------
    private JTextField txPath;
    private JComboBox cmbDelim;
    private ActionListener actionListener;
    private JButton btnFetch;
    private JButton btnAgent;
    private JComboBox cmbEncode;

    private String delimiter;
    private int maxColumn;
    private JList listP;
    private String fontName = "Dialog";// �t�H���g�� "�l�r �S�V�b�N";
    private boolean optSub = true; // �T�u�f�B���N�g���ȉ����������邩�ǂ���

    // -----------------------------------------------------------------------
    // �e�L�X�g�f�[�^���ǂ������聨�G���R�[�h�𔻒�̐��`���~����
    // SUBSTRING�v���O�����E�E�E�Œ蒷�w��E�E�E�S�p�����p�ϊ��A���p���S�p�ϊ���ǉ�����
    // �N���N�x�ϊ��A�敪�̑Ή��\���g�����ϊ����ł���ƂȂ��ǂ����E�E�E����͕ʃv���O����
    // -----------------------------------------------------------------------
    // �R���X�g���N�^
    // -----------------------------------------------------------------------
    public PnlFile(HashMap hmap) {
        super();
        this.caption = ParamMapUtil.getAsString(hmap, CAPTION, "file:");
        this.opt49er = ParamMapUtil.getAsBoolean(hmap, OPT49ER, true);
        this.optEncode = ParamMapUtil.getAsBoolean(hmap, ENCODE, false);
        this.optDelim = ParamMapUtil.getAsBoolean(hmap, DELIM, false);
        this.defaultPath = ParamMapUtil.getAsString(hmap, DEFAULT_PATH, "");
        this.captionWidth = ParamMapUtil.getAsInt(hmap, CAPTION_WIDTH, -1);
        this.useragent = ParamMapUtil.getAsBoolean(hmap, USER_AGENT, false);
        initGui();
        setText(defaultPath);
    }

    private void updateList() {
        if (listP == null) {
            return;
        }
        listP.setListData(new java.util.Vector()); // ��̕\��
        Thread th1 = new Thread() {
            @Override
            public void run() {
                java.util.List list = DosEmu.dir(currentPath, optSub);
                String[] array = (String[]) list
                        .toArray(new String[list.size()]);
                listP.setListData(array);
            }
        };
        th1.start();
    }

    // -----------------------------------------------------------------------
    // �A�N�Z�b�T
    // -----------------------------------------------------------------------
    public void setText(String path) {
        txPath.setText(path);
    }

    public String getPath() {
        return currentPath.trim();
    }

    public void setPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getDelimiter() {
        String delimiter = "";
        String wSel = (String) cmbDelim.getSelectedItem();
        if (wSel.equals(COMMNA)) {
            delimiter = ",";
        } // csv,txt,tmp,bak
        else if (wSel.equals("Tabcode")) {
            delimiter = "\t";
        } // prn,dat,old
        else if (wSel.equals("Space")) {
            delimiter = " ";
        } else if (wSel.equals("@")) {
            delimiter = "@";
        } else if (wSel.equals(":")) {
            delimiter = ":";
        } else if (wSel.equals(";")) {
            delimiter = ";";
        } else if (wSel.equals(".")) {
            delimiter = ".";
        } else if (wSel.equals("*")) {
            delimiter = "*";
        } else if (wSel.equals("-")) {
            delimiter = "-";
        } else if (wSel.equals("_")) {
            delimiter = "_";
        } else if (wSel.equals("|")) {
            delimiter = "|";
        } else {
            delimiter = wSel;
        }
        return delimiter;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public void setTargetList(JList jList) {
        this.listP = jList;
    }

    public String getEncode() {
        return (String) cmbEncode.getSelectedItem();
    }

    // �����̕������߂�
    private int stringWidth(Font font, String str) {
        FontMetrics fm = getFontMetrics(font);
        return fm.stringWidth(str);
    }

    public void setOptSub(boolean optSub) {
        this.optSub = optSub;
    }
    // public void paint(Graphics g) {
    // super.paint(g); // Super�o�R��paintComp�����
    // }
    // public void paintComponent(Graphics g) {
    // kyPkg.util.Ruler.drawRuler(g, this.getSize().width,
    // this.getSize().height, Color.GREEN);
    // }

    private void initGui() {
        Font font1 = new Font(fontName, Font.PLAIN, 10);// "Dialog"
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        txPath = new JTextField(currentPath);
        txPath.setFont(font1);
        if (captionWidth < 0) {
            captionWidth = stringWidth(font1, caption) + 15;
        }

        JLabel labCaption = new JLabel(caption);
        labCaption.setFont(font1);
        labCaption.setOpaque(false);
        labCaption.setPreferredSize(new Dimension(captionWidth, 20));
        labCaption.setSize(new Dimension(captionWidth, 20));
        new java.awt.dnd.DropTarget(txPath, new DnDAdapter(txPath));

        // --------------------------------------------------------------------
        // Encode
        // --------------------------------------------------------------------
        cmbEncode = new JComboBox();
        cmbEncode.setPreferredSize(new Dimension(70, 20));
        List charsetNames = FileUtil.getCharsetNames();
        for (Iterator iterator = charsetNames.iterator(); iterator.hasNext();) {
            String charsetName = (String) iterator.next();
            cmbEncode.addItem(charsetName);
        }
        //String charsetName = System.getProperty("file.encoding");
        String charsetName = FileUtil.getDefaultEncoding();//20161222
        cmbEncode.setSelectedItem(charsetName);

        // --------------------------------------------------------------------
        // Delimiter
        // --------------------------------------------------------------------
        cmbDelim = new JComboBox();
        cmbDelim.setPreferredSize(new Dimension(80, 20));
        cmbDelim.addItem(COMMNA); // csv,txt,tmp,bak
        cmbDelim.addItem("Tabcode"); // prn,dat,old
        cmbDelim.addItem("Space");
        cmbDelim.addItem("@");
        cmbDelim.addItem(":");
        cmbDelim.addItem(";");
        cmbDelim.addItem(".");
        cmbDelim.addItem("*");
        cmbDelim.addItem("-");
        cmbDelim.addItem("_");
        cmbDelim.addItem("|");

        // cmbDelim.addItem("\\");
        // JPanel pnlTitle = new JPanel(new FlowLayout());
        // pnlTitle.setOpaque(false);
        // pnlTitle.add(labCaption);
        // JPanel pnlOption = new JPanel(new FlowLayout());
        btnFetch = new JButton("...");
        btnFetch.setPreferredSize(new Dimension(30, 18));
        btnFetch.setSize(new Dimension(30, 18));
        btnFetch.setFont(font1);

        btnAgent = new JButton("OPEN");
        btnAgent.setPreferredSize(new Dimension(80, 20));
        btnAgent.setSize(new Dimension(30, 20));
        btnAgent.setFont(font1);

		JPanel pnlOption = new JPanel(new BorderLayout());
		pnlOption.setOpaque(false);
		pnlOption.add(btnFetch, BorderLayout.LINE_START);
		if (optEncode) {
			pnlOption.add(cmbEncode, BorderLayout.CENTER);
		}
		if (useragent) {
			pnlOption.add(btnAgent, BorderLayout.LINE_END);
		}
		if (optDelim) {
			pnlOption.add(cmbDelim, BorderLayout.LINE_END);
		}

//        JPanel pnlOption = new JPanel(new FlowLayout());
//        pnlOption.setOpaque(false);
//        pnlOption.add(btnFetch);
//        if (optEncode) {
//            pnlOption.add(cmbEncode);
//        }
//        if (useragent) {
//            pnlOption.add(btnAgent);
//        }
//        if (optDelim) {
//            pnlOption.add(cmbDelim);
//        }

        this.add(labCaption, BorderLayout.LINE_START);
        this.add(txPath, BorderLayout.CENTER);
        this.add(pnlOption, BorderLayout.LINE_END);

        checkIt("@PnlFile ### initGui ###");

        // --------------------------------------------------------------------
        // Encode���ύX���ꂽ
        // --------------------------------------------------------------------
        cmbEncode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            }
        });
        // --------------------------------------------------------------------
        // �g���q���ύX���ꂽ�E�E�E�E���O�̂��̂Ɠ������ǂ������ׂ������E�E�E
        // --------------------------------------------------------------------
        cmbDelim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            }
        });
        // --------------------------------------------------------------------
        // "..."�{�^���������ꂽ
        // --------------------------------------------------------------------
        btnFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btnFetch.setEnabled(false);
                JFileChooser fc = new JFileChooser(currentPath);
                // fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                // fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    setText(fc.getSelectedFile().toString());
                    kickOut(ae);
                }
                btnFetch.setEnabled(true);
            }
        });

        btnAgent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String path = txPath.getText();
                path = path.trim();
                if (!path.equals("")) {
                    UserAgent.openWithUA(path);
                }
            }
        });
        // -----------------------------------------------------------
        txPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkIt("### �����܂��͑����Z�b�g���ύX���ꂽ ###");
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkIt("### �h�L�������g�ւ̑}���������� ###"); //	�h���b�O�h���b�v�̏ꍇ�Ȃ�
                ActionEvent ev = null;//dummy
                kickOut(ev);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkIt("### �h�L�������g�̈ꕔ���폜���ꂽ ###");
            }
        });
    }

    // ���炩���ߓo�^���ꂽ�A�N�V���������s����
    private void kickOut(ActionEvent ae) {
        setHourglass(this, true);//�����v�\��
        if (actionListener != null) {
            actionListener.actionPerformed(ae);
        }
        setHourglass(this, false);//�����v�\��
    }

    private void checkIt(String message) {
        System.out.println(message + txPath.getText());
        currentPath = evalPath(txPath.getText());
        updateList();
    }

    private String evalPath(String path) {
        path = path.trim();
        delimiter = "";
        maxColumn = -1;
        if (path.toLowerCase().endsWith(".xls")) {
            return path;
        }
        File wFile = new File(path);
        if (wFile.isDirectory()) {
            if (!path.endsWith(FSP)) {
                path = path + FSP;
            }
        } else if (wFile.isFile() && wFile.exists()) {
            if (opt49er) {
                // �t�@�C�����ǂ݂��āE�E�ő�̋�؂蕶���A�J���������E��
                File49ers f49 = new File49ers(path);
                delimiter = f49.getDelimiter();
                maxColumn = f49.getMaxColCount();
                if (delimiter.equals(",")) {
                    cmbDelim.setSelectedItem(COMMNA); // csv,txt,tmp,bak
                } else if (delimiter.equals("\t")) {
                    cmbDelim.setSelectedItem("Tabcode"); // prn,dat,old
                } else {
                    cmbDelim.setSelectedItem("Space");
                }
            }
        }
        return path;
    }
}
