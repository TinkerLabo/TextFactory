package kyPkg.util;

import static kyPkg.uFile.FileUtil.getExt;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import sun.awt.shell.ShellFolder;

import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.List;
//-----+---------+---------+---------+---------+---------+---------+---------+
// DnDAdapter
//-----+---------+---------+---------+---------+---------+---------+---------+

import kyPkg.uFile.FileUtil;

/**
 * ****************************************************************************
 * 《使用例》 final JTextField jTf1 = new JTextField("data.csv");
 * jTf1.setBounds(100,10,300,20); this.add(jTf1); new
 * java.awt.dnd.DropTarget(jTf1,new DnDAdapter(jTf1)); // dnd.DropTarget
 *****************************************************************************
 */
public class DnDAdapter implements DropTargetListener {

    private Object targetObj;
    private Object optionObj;
    private Color foreColor = Color.WHITE;
    private Color backColor = new Color(128, 128, 255);

    // -------------------------------------------------------------------------
    // アクセッサ
    // -------------------------------------------------------------------------
    public void setForeColor(Color foreColor) {
        this.foreColor = foreColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    private void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    public void setOptionObj(Object optionObj) {
        this.optionObj = optionObj;
    }

    // -------------------------------------------------------------------------
    // ドラッグドロップされたときの処理(オーバーライドさせて別の動きをさせてもいいと思う)
    // -------------------------------------------------------------------------
    public void setTargetText(String str) {
        if (targetObj != null) {
            if (targetObj instanceof JTextComponent) {
                System.out.println(
                        "#debug20150403# setTargetText JTextComponent:" + str);

                str = getLinkLocation(str);//20150406リンクがドロップされた時の処理

                if (targetObj instanceof JTextField) {
                    ((JTextField) targetObj).setText(str);
                } else if (targetObj instanceof JTextArea) {
                    String wExt = getExt(str);
                    System.out.println("debug ext=>" + wExt);
                    if (wExt.equals("TXT") || wExt.equals("CSV")
                            || wExt.equals("IT2") || wExt.equals("JAN")
                            || wExt.equals("")) {
                        String val = FileUtil.file2String(str);
                        // strをパスとみなして・・・このファイルを読み込んでしまう
                        ((JTextArea) targetObj).setText(val);
                    }
                }
            } else if (targetObj instanceof JComboBox) {
                System.out.println(
                        "#debug20150403# setTargetText JComboBox:" + str);

                ((JComboBox) targetObj).addItem(str);
                ((JComboBox) targetObj).setSelectedItem(str);
            }
        }
    }

    public void setOptionText(String str) {
        if (optionObj != null && optionObj instanceof JTextComponent) {
            ((JTextField) optionObj).setText(str);
        }
    }

    // -------------------------------------------------------------------------
    // コンストラクタ
    // -------------------------------------------------------------------------
    public DnDAdapter() {
        this(null);
    }

    public DnDAdapter(Object pTargetObj) {
        setTargetObj(pTargetObj);
    }

    public DnDAdapter(Object pTargetObj, Color backColor) {
        setTargetObj(pTargetObj);
        setBackColor(backColor);
    }

    /**
     * ********************************************************************
     * ドラッグ操作を検出したときに呼び出されます．
     *
     * @param dtde ドラッグ＆ドロップイベント
	 *********************************************************************
     */
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(DnDConstants.ACTION_COPY);
    }

    /**
     * ********************************************************************
     * ドラッグ操作がドロップなしのときに呼び出されます．
     *
     * @param dte ドラッグ＆ドロップイベント
	 *********************************************************************
     */
    @Override
    public void dragExit(DropTargetEvent dte) {
        if (targetObj != null && targetObj instanceof JComponent) {
            ((JComponent) targetObj).setBackground(Color.white);
            ((JComponent) targetObj).setBackground(Color.BLACK);
        }
    }

    /**
     * ********************************************************************
     * ドラッグ操作が進行中のときに呼び出されます．
     *
     * @param dte ドラッグ＆ドロップイベント
	 *********************************************************************
     */
    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        if (targetObj != null && targetObj instanceof JComponent) {
            ((JComponent) targetObj).setForeground(foreColor);
            ((JComponent) targetObj).setBackground(backColor);
        }
    }

    /**
     * ********************************************************************
     * ドロップ完了！
     *
     * @param dte ドラッグ＆ドロップイベント
	 *********************************************************************
     */
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();
            // サポート可能か判定
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE); // ドロップを受け入れる
                // 転送データからファイルのリストを取得する
                List myList = (List) tr
                        .getTransferData(DataFlavor.javaFileListFlavor);
                // リスト要素が単一かどうか判定
                if (myList.size() == 1) {
                    File myFile = (File) myList.get(0); // リストからファイルの取り出し
                    String wVal = myFile.getAbsolutePath();
                    String[] array = wVal.split("\\.");
                    setTargetText(wVal);
                    if (array.length > 0) {
                        setOptionText(array[0]);
                    } else {
                        System.out.println("●～*　array.length:" + array.length);
                        System.out.println("●～*　wVal:" + wVal);
                    }
                    dtde.getDropTargetContext().dropComplete(true); // ドロップ動作を正常終了
                } else {
                    // System.out.println("Too much elements.");
                    dtde.getDropTargetContext().dropComplete(false); // 異常終了
                }
            } else {
                // System.out.println("Unsupported.");
                dtde.rejectDrop();
            }
        } catch (IOException ioe) {
            // System.out.println("I/O exception.");
            dtde.rejectDrop();
        } catch (UnsupportedFlavorException ufe) {
            // new Msgbox(this).error("Unsupported");
            dtde.rejectDrop();
        }
    }

    /**
     * ********************************************************************
     * ユーザが現在のドロップジェスチャーを変更したときに呼び出されます．
     *
     * @param dte ドラッグ＆ドロップイベント
	 *********************************************************************
     */
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    // ----------------------------------------------------------------
    // D&Dされた結果ファイルの終端が.lnkならリンク先のパスに置き換えるロジックを作りたい20150406
    // ----------------------------------------------------------------
    public static String getLinkLocation(String dir) {
        if (dir.endsWith(".lnk")) {
            try {
                File linkedLocation = getLinkLocation(dir, true);
                dir = linkedLocation.getAbsolutePath();
                System.out.println("#debug#20150406 dest=>" + dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dir;
    }

    // ----------------------------------------------------------------
    // リンクファイルの参照先を返す (20150406)
    // ----------------------------------------------------------------
    public static File getLinkLocation(String path, boolean debug) {
        if (path != null) {
            try {
                File linkedTo = new File(path);
                ShellFolder shellFolder = ShellFolder.getShellFolder(linkedTo);
                if (shellFolder.isLink()) {
                    linkedTo = shellFolder.getLinkLocation();
                }
                return linkedTo;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
