package com.vinyl.assignment1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vinyl {
  // JavaFX Properties 用于支持 UI 双向数据绑定 (Data Binding)
  private final StringProperty title;
  private final StringProperty artist;
  private final IntegerProperty releaseYear;
  private final ObjectProperty<VinylState> state;
  private final StringProperty borrowingUserId;
  private final StringProperty reservingUserId;
  private final BooleanProperty markedForRemoval;

  public Vinyl(String title, String artist, int releaseYear) {
    this.title = new SimpleStringProperty(title);
    this.artist = new SimpleStringProperty(artist);
    this.releaseYear = new SimpleIntegerProperty(releaseYear);
    // 初始状态流转节点
    this.state = new SimpleObjectProperty<>(new AvailableState());
    this.borrowingUserId = new SimpleStringProperty(null);
    this.reservingUserId = new SimpleStringProperty(null);
    this.markedForRemoval = new SimpleBooleanProperty(false);
  }

  // --- Title ---
  public String getTitle() { return title.get(); }
  public void setTitle(String newTitle) { title.set(newTitle); }
  public StringProperty titleProperty() { return title; }

  // --- Artist ---
  public String getArtist() { return artist.get(); }
  public void setArtist(String newArtist) { artist.set(newArtist); }
  public StringProperty artistProperty() { return artist; }

  // --- Release Year ---
  public int getReleaseYear() { return releaseYear.get(); }
  public void setReleaseYear(int newReleaseYear) { releaseYear.set(newReleaseYear); }
  public IntegerProperty releaseYearProperty() { return releaseYear; }

  // --- State ---
  public VinylState getState() { return state.get(); }
  public void setState(VinylState newState) { state.set(newState); }
  public ObjectProperty<VinylState> stateProperty() { return state; }

  // --- Borrowing User ID ---
  public String getBorrowingUserId() { return borrowingUserId.get(); }
  public void setBorrowingUserId(String userId) { borrowingUserId.set(userId); }
  public StringProperty borrowingUserIdProperty() { return borrowingUserId; }

  // --- Reserving User ID ---
  public String getReservingUserId() { return reservingUserId.get(); }
  public void setReservingUserId(String userId) { reservingUserId.set(userId); }
  public StringProperty reservingUserIdProperty() { return reservingUserId; }

  // --- Marked For Removal (软删除标记) ---
  public boolean isMarkedForRemoval() { return markedForRemoval.get(); }
  public void setMarkedForRemoval(boolean marked) { markedForRemoval.set(marked); }
  public BooleanProperty markedForRemovalProperty() { return markedForRemoval; }

  // --- 状态判定辅助方法 ---
  public boolean isReserved() {
    return getReservingUserId() != null && !getReservingUserId().trim().isEmpty();
  }

  public boolean isBorrowed() {
    return getBorrowingUserId() != null && !getBorrowingUserId().trim().isEmpty();
  }

  // --- 状态模式 (State Pattern) 指派转交 ---
  public boolean reserve(String userId) {
      return state.get().reserve(this, userId);
  }

  public boolean borrow(String userId) {
      return state.get().borrow(this, userId);
  }

  public void returnVinyl() {
      state.get().returnVinyl(this);
  }

  public VinylDTO toDTO() {
    return new VinylDTO(
      getTitle(),
      getArtist(),
      getReleaseYear(),
      getState() == null ? "Available" : getState().getStateName(),
      getBorrowingUserId(),
      getReservingUserId(),
      isMarkedForRemoval()
    );
  }

  public static Vinyl fromDTO(VinylDTO dto) {
    Vinyl vinyl = new Vinyl(dto.getTitle(), dto.getArtist(), dto.getReleaseYear());
    vinyl.setBorrowingUserId(dto.getBorrowingUserId());
    vinyl.setReservingUserId(dto.getReservingUserId());
    vinyl.setMarkedForRemoval(dto.isMarkedForRemoval());
    if (dto.getState() != null) {
      switch (dto.getState()) {
        case "Reserved":
          vinyl.setState(new ReservedState());
          break;
        case "Borrowed":
          vinyl.setState(new BorrowedState());
          break;
        case "Borrowed & Reserved":
          vinyl.setState(new BorrowedAndReservedState());
          break;
        case "Available":
        default:
          vinyl.setState(new AvailableState());
          break;
      }
    }
    return vinyl;
  }
}
