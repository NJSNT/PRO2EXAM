package com.vinyl.assignment1.viewmodel;

import com.vinyl.assignment1.model.VinylLibrary;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// 添加唱片视图模型 (AddVinylViewModel)
public class AddVinylViewModel {
    
    private final VinylLibrary library;

    // View 双向绑定属性
    private final StringProperty title;
    private final StringProperty artist;
    private final StringProperty releaseYear;
    private final StringProperty errorLabel; // 错误信息提示

    public AddVinylViewModel(VinylLibrary library) {
        this.library = library;
        this.title = new SimpleStringProperty("");
        this.artist = new SimpleStringProperty("");
        this.releaseYear = new SimpleStringProperty("");
        this.errorLabel = new SimpleStringProperty("");
    }

    // --- Title ---
    public String getTitle() { return title.get(); }
    public void setTitle(String t) { title.set(t); }
    public StringProperty titleProperty() { return title; }

    // --- Artist ---
    public String getArtist() { return artist.get(); }
    public void setArtist(String a) { artist.set(a); }
    public StringProperty artistProperty() { return artist; }

    // --- Release Year ---
    public String getReleaseYear() { return releaseYear.get(); }
    public void setReleaseYear(String ry) { releaseYear.set(ry); }
    public StringProperty releaseYearProperty() { return releaseYear; }

    // --- Error Label ---
    public String getErrorLabel() { return errorLabel.get(); }
    public void setErrorLabel(String e) { errorLabel.set(e); }
    public StringProperty errorLabelProperty() { return errorLabel; }

    // --- 核心业务验证逻辑: 提交新增唱片 ---
    public boolean addVinyl() {
        String t = getTitle();
        String a = getArtist();
        String y = getReleaseYear();

        // 1. 字段非空验证
        if (t == null || t.trim().isEmpty() || 
            a == null || a.trim().isEmpty() || 
            y == null || y.trim().isEmpty()) {
            setErrorLabel("All fields are required!");
            return false;
        }

        // 2. 年份格式与合法逻辑验证
        int yearValue;
        try {
            yearValue = Integer.parseInt(y.trim());
            // 校验时间线不可早于 1800 或迟于当前年代
            if (yearValue < 1800 || yearValue > java.time.Year.now().getValue()) {
                setErrorLabel("Please enter a valid release year.");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorLabel("Release year must be a number!");
            return false;
        }

        // 3. 校验通过, 入库流转
        library.addVinyl(t.trim(), a.trim(), yearValue);
        clearForm();
        return true; 
    }

    // 清空缓存上下文信息以备复用
    private void clearForm() {
        setTitle("");
        setArtist("");
        setReleaseYear("");
        setErrorLabel("");
    }
}
