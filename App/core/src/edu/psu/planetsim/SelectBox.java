package edu.psu.planetsim;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.sun.istack.internal.Nullable;

public class SelectBox<T> extends Widget implements Disableable {

    static final Vector2 temp = new Vector2();

    SelectBoxStyle style;
    final Array<T> items = new Array();
    final ArraySelection<T> selection = new ArraySelection(items);
    SelectBoxList<T> selectBoxList;
    private float prefWidth, prefHeight;
    private ClickListener clickListener;
    boolean disabled;
    private int alignment = Align.left;

    public SelectBox (Skin skin) {
        this(skin.get(SelectBoxStyle.class));
    }

    public SelectBox (Skin skin, String styleName) {
        this(skin.get(styleName, SelectBoxStyle.class));
    }

    public SelectBox (SelectBoxStyle style) {
        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());

        selection.setActor(this);
        selection.setRequired(true);

        selectBoxList = new SelectBoxList(this);

        addListener(clickListener = new ClickListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) return false;
                if (disabled) return false;
                if (selectBoxList.hasParent())
                    hideList();
                else
                    showList();
                return true;
            }
        });
    }

    public void setMaxListCount (int maxListCount) {
        selectBoxList.maxListCount = maxListCount;
    }

    public int getMaxListCount () {
        return selectBoxList.maxListCount;
    }

    protected void setStage (Stage stage) {
        if (stage == null) selectBoxList.hide();
        super.setStage(stage);
    }

    public void setStyle (SelectBoxStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        if (selectBoxList != null) {
            selectBoxList.setStyle(style.scrollStyle);
            selectBoxList.list.setStyle(style.listStyle);
        }
        invalidateHierarchy();
    }

    public SelectBoxStyle getStyle () {
        return style;
    }

    public void setItems (T... newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth();

        items.clear();
        items.addAll(newItems);
        selection.validate();
        selectBoxList.list.setItems(items);

        invalidate();
        if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
    }

    public void setItems (Array<T> newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth();

        if (newItems != items) {
            items.clear();
            items.addAll(newItems);
        }
        selection.validate();
        selectBoxList.list.setItems(items);

        invalidate();
        if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
    }

    public void clearItems () {
        if (items.size == 0) return;
        items.clear();
        selection.clear();
        invalidateHierarchy();
    }

    public Array<T> getItems () {
        return items;
    }

    public void layout () {
        Drawable bg = style.background;
        BitmapFont font = style.font;

        if (bg != null) {
            prefHeight = Math.max(bg.getTopHeight() + bg.getBottomHeight() + font.getCapHeight() - font.getDescent() * 2,
                    bg.getMinHeight());
        } else
            prefHeight = font.getCapHeight() - font.getDescent() * 2;

        float maxItemWidth = 0;
        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        for (int i = 0; i < items.size; i++) {
            layout.setText(font, toString(items.get(i)));
            maxItemWidth = Math.max(layout.width, maxItemWidth);
        }
        layoutPool.free(layout);

        prefWidth = maxItemWidth;
        if (bg != null) prefWidth = Math.max(prefWidth + bg.getLeftWidth() + bg.getRightWidth(), bg.getMinWidth());

        ListStyle listStyle = style.listStyle;
        ScrollPaneStyle scrollStyle = style.scrollStyle;
        float listWidth = maxItemWidth + listStyle.selection.getLeftWidth() + listStyle.selection.getRightWidth();
        bg = scrollStyle.background;
        if (bg != null) listWidth = Math.max(listWidth + bg.getLeftWidth() + bg.getRightWidth(), bg.getMinWidth());
        if (selectBoxList == null ) {
            listWidth += Math.max(style.scrollStyle.vScroll != null ? style.scrollStyle.vScroll.getMinWidth() : 0,
                    style.scrollStyle.vScrollKnob != null ? style.scrollStyle.vScrollKnob.getMinWidth() : 0);
        }
        prefWidth = Math.max(prefWidth, listWidth);
    }

    public void draw (Batch batch, float parentAlpha) {
        validate();

        Drawable background;
        if (disabled && style.backgroundDisabled != null)
            background = style.backgroundDisabled;
        else if (selectBoxList.hasParent() && style.backgroundOpen != null)
            background = style.backgroundOpen;
        else if (clickListener.isOver() && style.backgroundOver != null)
            background = style.backgroundOver;
        else if (style.background != null)
            background = style.background;
        else
            background = null;
        BitmapFont font = style.font;
        Color fontColor = (disabled && style.disabledFontColor != null) ? style.disabledFontColor : style.fontColor;

        Color color = getColor();
        float x = getX(), y = getY();
        float width = getWidth(), height = getHeight();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (background != null) background.draw(batch, x, y, width, height);

        T selected = selection.first();
        if (selected != null) {
            if (background != null) {
                width -= background.getLeftWidth() + background.getRightWidth();
                height -= background.getBottomHeight() + background.getTopHeight();
                x += background.getLeftWidth();
                y += (int)(height / 2 + background.getBottomHeight() + font.getData().capHeight / 2);
            } else {
                y += (int)(height / 2 + font.getData().capHeight / 2);
            }
            font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
            drawItem(batch, font, selected, x, y, width);
        }
    }

    protected GlyphLayout drawItem (Batch batch, BitmapFont font, T item, float x, float y, float width) {
        String string = toString(item);
        return font.draw(batch, string, x, y, 0, string.length(), width, alignment, false, "...");
    }

    public void setAlignment (int alignment) {
        this.alignment = alignment;
    }

    public ArraySelection<T> getSelection () {
        return selection;
    }

    @Nullable
    public T getSelected () {
        return selection.first();
    }

    public void setSelected (@Nullable T item) {
        if (items.contains(item, false))
            selection.set(item);
        else if (items.size > 0)
            selection.set(items.first());
        else
            selection.clear();
    }

    public int getSelectedIndex () {
        ObjectSet<T> selected = selection.items();
        return selected.size == 0 ? -1 : items.indexOf(selected.first(), false);
    }

    public void setSelectedIndex (int index) {
        selection.set(items.get(index));
    }

    public void setDisabled (boolean disabled) {
        if (disabled && !this.disabled) hideList();
        this.disabled = disabled;
    }

    public boolean isDisabled () {
        return disabled;
    }

    public float getPrefWidth () {
        validate();
        return prefWidth;
    }

    public float getPrefHeight () {
        validate();
        return prefHeight;
    }

    protected String toString (T item) {
        return item.toString();
    }

    public void showList () {
        if (items.size == 0) return;
        if (getStage() != null) selectBoxList.show(getStage());
    }

    public void hideList () {
        selectBoxList.hide();
    }

    public List<T> getList () {
        return selectBoxList.list;
    }

    public void setScrollingDisabled (boolean y) {
        selectBoxList.setScrollingDisabled(true, y);
        invalidateHierarchy();
    }

    public ScrollPane getScrollPane () {
        return selectBoxList;
    }

    protected void onShow (Actor selectBoxList, boolean below) {
        selectBoxList.getColor().a = 0;
        selectBoxList.addAction(fadeIn(0.3f, Interpolation.fade));
    }

    protected void onHide (Actor selectBoxList) {
        selectBoxList.getColor().a = 1;
        selectBoxList.addAction(sequence(fadeOut(0.15f, Interpolation.fade), removeActor()));
    }

    static class SelectBoxList<T> extends ScrollPane {
        private final SelectBox<T> selectBox;
        int maxListCount;
        private final Vector2 screenPosition = new Vector2();
        final List<T> list;
        private InputListener hideListener;
        private Actor previousScrollFocus;

        public SelectBoxList (final SelectBox<T> selectBox) {
            super(null, selectBox.style.scrollStyle);
            this.selectBox = selectBox;

            setOverscroll(false, false);
            setFadeScrollBars(false);
            setScrollingDisabled(true, false);

            list = new List<T>(selectBox.style.listStyle) {
                public String toString (T obj) {
                    return selectBox.toString(obj);
                }
            };
            list.setTouchable(Touchable.disabled);
            list.setTypeToSelect(true);
            setActor(list);

            list.addListener(new ClickListener() {
                public void clicked (InputEvent event, float x, float y) {
                    selectBox.selection.choose(list.getSelected());
                    hide();
                }

                public boolean mouseMoved (InputEvent event, float x, float y) {
                    int index = list.getItemIndexAt(y);
                    if (index != -1) list.setSelectedIndex(index);
                    return true;
                }
            });

            addListener(new InputListener() {
                public void exit (InputEvent event, float x, float y, int pointer, @Nullable Actor toActor) {
                    if (toActor == null || !isAscendantOf(toActor)) {
                        //list.selection.set(list.getSelected());
                    }
                }
            });

            hideListener = new InputListener() {
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    Actor target = event.getTarget();
                    if (isAscendantOf(target)) return false;
                    //list.selection.set(list.getSelected());
                    hide();
                    return false;
                }

                public boolean keyDown (InputEvent event, int keycode) {
                    switch (keycode) {
                        case Keys.ENTER:
                            selectBox.selection.choose(list.getSelected());
                            // Fall thru.
                        case Keys.ESCAPE:
                            hide();
                            event.stop();
                            return true;
                    }
                    return false;
                }
            };
        }

        public void show (Stage stage) {
            if (list.isTouchable()) return;

            stage.addActor(this);
            stage.addCaptureListener(hideListener);
            stage.addListener(list.getKeyListener());

            selectBox.localToStageCoordinates(screenPosition.set(0, 0));

            // Show the list above or below the select box, limited to a number of items and the available height in the stage.
            float itemHeight = list.getItemHeight();
            float height = itemHeight * (maxListCount <= 0 ? selectBox.items.size : Math.min(maxListCount, selectBox.items.size));
            Drawable scrollPaneBackground = getStyle().background;
            if (scrollPaneBackground != null) height += scrollPaneBackground.getTopHeight() + scrollPaneBackground.getBottomHeight();
            Drawable listBackground = list.getStyle().background;
            if (listBackground != null) height += listBackground.getTopHeight() + listBackground.getBottomHeight();

            float heightBelow = screenPosition.y;
            float heightAbove = stage.getCamera().viewportHeight - screenPosition.y - selectBox.getHeight();
            boolean below = true;
            if (height > heightBelow) {
                if (heightAbove > heightBelow) {
                    below = false;
                    height = Math.min(height, heightAbove);
                } else
                    height = heightBelow;
            }

            if (below)
                setY(screenPosition.y - height);
            else
                setY(screenPosition.y + selectBox.getHeight());
            setX(screenPosition.x);
            setHeight(height);
            validate();
            float width = Math.max(getPrefWidth(), selectBox.getWidth());
            if (getPrefHeight() > height && !isScrollingDisabledY()) width += getScrollBarWidth();
            setWidth(width);

            validate();
            scrollTo(0, list.getHeight() - selectBox.getSelectedIndex() * itemHeight - itemHeight / 2, 0, 0, true, true);
            updateVisualScroll();

            previousScrollFocus = null;
            Actor actor = stage.getScrollFocus();
            if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;
            stage.setScrollFocus(this);

            //list.selection.set(selectBox.getSelected());
            list.setTouchable(Touchable.enabled);
            clearActions();
            selectBox.onShow(this, below);
        }

        public void hide () {
            if (!list.isTouchable() || !hasParent()) return;
            list.setTouchable(Touchable.disabled);

            Stage stage = getStage();
            if (stage != null) {
                stage.removeCaptureListener(hideListener);
                stage.removeListener(list.getKeyListener());
                if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
                Actor actor = stage.getScrollFocus();
                if (actor == null || isAscendantOf(actor)) stage.setScrollFocus(previousScrollFocus);
            }

            clearActions();
            selectBox.onHide(this);
        }

        public void draw (Batch batch, float parentAlpha) {
            selectBox.localToStageCoordinates(temp.set(0, 0));
            if (!temp.equals(screenPosition)) hide();
            super.draw(batch, parentAlpha);
        }

        public void act (float delta) {
            super.act(delta);
            toFront();
        }

        protected void setStage (Stage stage) {
            Stage oldStage = getStage();
            if (oldStage != null) {
                oldStage.removeCaptureListener(hideListener);
                oldStage.removeListener(list.getKeyListener());
            }
            super.setStage(stage);
        }
    }


    static public class SelectBoxStyle {
        public BitmapFont font;
        public Color fontColor = new Color(1, 1, 1, 1);

        @Nullable public Color disabledFontColor;

        @Nullable public Drawable background;
        public ScrollPaneStyle scrollStyle;
        public ListStyle listStyle;
   
        @Nullable public Drawable backgroundOver, backgroundOpen, backgroundDisabled;

        public SelectBoxStyle () {
        }

        public SelectBoxStyle (BitmapFont font, Color fontColor, @Nullable Drawable background, ScrollPaneStyle scrollStyle,
                               ListStyle listStyle) {
            this.font = font;
            this.fontColor.set(fontColor);
            this.background = background;
            this.scrollStyle = scrollStyle;
            this.listStyle = listStyle;
        }

        public SelectBoxStyle (SelectBoxStyle style) {
            this.font = style.font;
            this.fontColor.set(style.fontColor);
            if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor);
            this.background = style.background;
            this.backgroundOver = style.backgroundOver;
            this.backgroundOpen = style.backgroundOpen;
            this.backgroundDisabled = style.backgroundDisabled;
            this.scrollStyle = new ScrollPaneStyle(style.scrollStyle);
            this.listStyle = new ListStyle(style.listStyle);
        }
    }


}
