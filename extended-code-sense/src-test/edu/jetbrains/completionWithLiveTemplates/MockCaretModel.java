package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 11.07.2010
 * Time: 21:05:30
 * To change this template use File | Settings | File Templates.
 */
public class MockCaretModel implements CaretModel {

    public int offset;

    public void moveCaretRelatively(int columnShift, int lineShift, boolean withSelection, boolean blockSelection, boolean scrollToCaret) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveToLogicalPosition(LogicalPosition pos) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveToVisualPosition(VisualPosition pos) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveToOffset(int offset) {
        this.offset = offset;
    }

    public LogicalPosition getLogicalPosition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public VisualPosition getVisualPosition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getOffset() {
        return offset;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addCaretListener(CaretListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeCaretListener(CaretListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getVisualLineStart() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getVisualLineEnd() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TextAttributes getTextAttributes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
