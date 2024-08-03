package com.TheMFG.HotelManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    /* REZERVASYON CLASS'ı */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Giriş Tarihi Boş Bırakılamaz") // tanımlanan değişkenin boş olmamasını sağlar.
    private LocalDate checkInDate; // giriş tarihi

    @Future(message = "Çıkış Tarihi, Giriş Tarihinden Sonraki Bir Tarih Olmalı!")
    private LocalDate checkOutDate; //çıkış tarihi

    @Min(value = 1,message = "Yetişkin Misafir Sayısı En Az 1 Kişi Olmalı!")
    private int numOfAdults; // yetişkin misafir sayısı

    @Min(value = 0, message = "Çocuk Misafir Sayısı En Az 0 Olmalı!")
    private int numOfChildren; // çocuk misafir sayisi

    private int totalNumOfGuest; // toplam misafir sayisi

    private String bookingConfirmationCode; //rezervasyon onay kodu

    /* booking class'ının bir 'user' nesnesiyle olan ilişkisini tanımlar (many - to - one)
    * '@ManyToOne' -> bir kullanıcının birden fazla rezervasyonu olabilir anlamında
    * 'FetchType.EAGER' -> bu nesne veritabanından çağrıldığında ilişkili nesnenin hemen yüklenmesini sağlar.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // yalnızca kullanılması gerektiğinde veritabanından çağrılır.
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults){
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren(int numOfChildren){
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", user=" + user +
                ", room=" + room +
                '}';
    }
}
