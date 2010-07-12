package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.EditorMouseEventArea;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MockEditor implements Editor {

    public Document document;
    @NotNull
    public Document getDocument() {
        return document;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isViewer() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public JComponent getComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public JComponent getContentComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public SelectionModel getSelectionModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public MarkupModel getMarkupModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public FoldingModel getFoldingModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public ScrollingModel getScrollingModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public CaretModel getCaretModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public EditorSettings getSettings() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public EditorColorsScheme getColorsScheme() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getLineHeight() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public Point logicalPositionToXY(@NotNull LogicalPosition pos) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int logicalPositionToOffset(@NotNull LogicalPosition pos) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public VisualPosition logicalToVisualPosition(@NotNull LogicalPosition logicalPos) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public Point visualPositionToXY(@NotNull VisualPosition visible) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public LogicalPosition visualToLogicalPosition(@NotNull VisualPosition visiblePos) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public LogicalPosition offsetToLogicalPosition(int offset) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public VisualPosition offsetToVisualPosition(int offset) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public LogicalPosition xyToLogicalPosition(@NotNull Point p) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public VisualPosition xyToVisualPosition(@NotNull Point p) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addEditorMouseListener(@NotNull EditorMouseListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeEditorMouseListener(@NotNull EditorMouseListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addEditorMouseMotionListener(@NotNull EditorMouseMotionListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeEditorMouseMotionListener(@NotNull EditorMouseMotionListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isDisposed() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Project getProject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isInsertMode() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isColumnMode() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isOneLineMode() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public EditorGutter getGutter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public EditorMouseEventArea getMouseEventArea(@NotNull MouseEvent e) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHeaderComponent(@Nullable JComponent header) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean hasHeaderComponent() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getHeaderComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IndentsModel getIndentsModel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getUserData(@NotNull Key<T> key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
