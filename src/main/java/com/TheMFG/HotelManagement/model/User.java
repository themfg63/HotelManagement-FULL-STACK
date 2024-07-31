package com.TheMFG.HotelManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email Boş Bırakılamaz")  //string türündeki değişkenlerin boş bırakılmamasını sağlar
    @Column(unique = true)  // alınan değerin veritabanında tek bir kaydı olmasını sağlar(benzersiz)
    private String email;

    @NotBlank(message = "Ad Soyad Boş Bırakılamaz")
    private String name;

    @NotBlank(message = "Telefon No Boş Bırakılamaz")
    private String phoneNumber;

    @NotBlank(message = "Şifre Boş Bırakılamaz")
    private String password;

    private String role;

    /*
    * bir user'ın birden fazla booking işlemi olabilir.
    * 'cascade = CascadeType.ALL' -> bu değişkene veritabanında ne işlem uygulanırsa bağlantılı olduğu değişkenlere de
    uygulanır.
     */
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>(); // booking classını yeni bir arrayliste atar.

    /*
    bu metot, bir kullanıcının yetkilerini tanımlamak ve güvenlik sistemi içinde bu yetkilerin tanınmasını
    sağlamak için kullanılır
    * 'Collection<? extends GrantedAuthority>' -> kullanıcının sahip olduğu yetkilerin bir koleksiyonunu döner.
    * 'List.of(new SimpleGrantedAuthority(role))' -> tek bir yetkiyi içeren bir liste oluşturur.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    // kullanıcının kimlik bilgisi olarak kullanılan kullanıcı adını döner.
    @Override
    public String getUsername() {
        return email;
    }

    // kullanıcı hesabının süresinin dolup dolmadığını belirtir.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // kullanıcı hesabının kilitli olup olmadığını belirtir.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //kullanıcı kimlik bilgilerinin süresinin dolup dolmadığını belirtir.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // kullanıcı hesabının etkin durumda olup olmadığını belirtir.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
