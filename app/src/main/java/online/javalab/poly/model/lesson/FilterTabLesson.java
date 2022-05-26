package online.javalab.poly.model.lesson;

import java.util.ArrayList;
import java.util.List;

import online.javalab.poly.utils.Define;

public class FilterTabLesson {
    private int id;
    private String title;
    private int icon;
    private boolean isSelected = false;

    public FilterTabLesson() {
    }

    public FilterTabLesson(int id, String title, int icon, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static List<FilterTabLesson> getDefaultTaps(){
        List<FilterTabLesson> list = new ArrayList<>();
        list.add(new FilterTabLesson(Define.Tab.TAB_ID_ALL,"All",0,true));
        list.add(new FilterTabLesson(Define.Tab.TAB_ID_NOT_START,"Not start",0,false));
        list.add(new FilterTabLesson(Define.Tab.TAB_ID_LEARNING,"Learning",0,false));
        list.add(new FilterTabLesson(Define.Tab.TAB_ID_DONE,"Done",0,false));
        return list;
    }
}
