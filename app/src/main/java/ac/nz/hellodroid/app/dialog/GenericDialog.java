package ac.nz.hellodroid.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Dale on 19/03/16.
 */
public abstract class GenericDialog implements AlertDialog.OnClickListener {
    private final AlertDialog.Builder dialogBuilder;
    private final String title;
    private final String message;
    private final String posBtnTitle;
    private final String negativeBtnTitle;
    private int viewResID;

    private <T extends Context> GenericDialog(T c, int viewResID, String title, String message, String posBtnTitle, String negBtnTitle) {
        this.title = title;
        this.viewResID = viewResID;
        this.message = message;
        this.posBtnTitle = posBtnTitle;
        this.negativeBtnTitle = negBtnTitle;
        dialogBuilder = new AlertDialog.Builder(c);
        populate();
    }

    public static void showDialog(Context c, int layoutRes, String title, String message, String posBtnName, String negBtnName, final AlertDialog.OnClickListener listener) {
        new GenericDialog(c, layoutRes, title, message, posBtnName, negBtnName) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        }.populate().show();
    }

    protected final Dialog populate() {
        return dialogBuilder.setNegativeButton(negativeBtnTitle.toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setView(viewResID).setTitle(title).setMessage(message).setPositiveButton(posBtnTitle, this).create();
    }
}
