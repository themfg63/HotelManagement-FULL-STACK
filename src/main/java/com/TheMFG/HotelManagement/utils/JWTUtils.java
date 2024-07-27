package com.TheMFG.HotelManagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    // EXPIRATION_TIME -> token'ın ne kadar süre geçerli olacağını belirten bir sabittir.
    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; // 7 GÜN

    private final SecretKey Key;

    /*
    Gizli bir anahtar(secret key) oluşturmak için kullanılır. Bu gizli anahtar, JWT'lerin imzalanması için kullanılır.

    * String secreteString -> adlı string değer JWT imzalama işlemi için kullanılacak olan ham gizli anaharı temsil eder.

    * byte[] keyBytes -> secreteString string değerini Base64 formatında çözerek bir byte dizisine dönüştürür.

    NOT: Base64 kodlama, binary verileri metin formatında temsil etmek için kullanılır ve
    burada çözme işlemi, gizli anahtarı binary formatına geri dönüştürür.

    * this.Key -> keyBytes dizisini kullanarak 'SecretKeySpec' adlı nesne oluşturur. Bu nesne, HMAC-SHA256 algoritması
    kullanarak JWT imzalamak için kullanılacak olan gizli anahtarı temsil eder.

    NOT: 'HmacSHA256' bir tür kriptografik hash fonksiyonudur ve JWT imzalama ve doğrulama işlemlerinde yayın olarak
    kullanılır.
     */
    public JWTUtils(){
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes,"HmacSHA256");
    }

    /*
    * Kullanıcıya özel bir JWT oluşturur ve bu belirteç, kullanıcıyı kimlik doğrulama süreçlerinde temsil eder.
    * Jwts.builder() -> JWT Oluşturma sürecini başlatır.
    * .subject(userDetails.getUsername()) -> JWT'nin konusu olarak kullanıcı adını ayarlar. Bu token'i kimin için oluş-
    turduğumuzu belirtir.
    * .issuedAt(new Date(System.currentTimeMillis())) -> Tokenin oluşturulma zamanını ayarlar. Bu, tokenin ne zaman ol-
    uşturulduğunu belirtir.
    * .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) -> Token'ın sona erme zamanını ayarlar.
    * .signWith(Key) -> Token'ı belirtilen anahtar ile imzalar. Bu, token'ın bütünlüğünü ve güvenliğini sağlar.
    * .compact() -> JWT'yi oluşturur ve string formatında döner.
     */
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    /*
    * extractUsername -> verilen bir JWT'den kullanıcı adını çıkarmak için kullanılır. Bu yöntem:
    ** 'token' adlı JWT'yi alır,
    ** 'extractClaims' yöntemini çağırarak JWT'den kullanıcı adını(subject) çıkarır.
    ** 'Claims::getSubject' ifadesi, 'Claims' nesnesindeki subject alanını alır. Bu alan genellikle kullanıcı
    adını içerir.
     */
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }


    /*
    * Genel bir JWT çözümleme ve içeriği (claims) çıkarma yöntemidir. Bu Yöntem:
    ** 'token' -> JWT'yi alır.
    ** 'claimsTFunction' -> adlı bir fonksiyon alır. Bu Fonksiyon, 'Claims' nesnesinden belirli bir bilgiyi çıkarmak
    için kullanılır.
    ** Jwts.parser() - > JWT'yi çözümlemek ve doğrulamak için kullanılır.
    ** 'verifyWith(Key)' -> JWT'nin imzasını doğrular. Bu token'ın bütünlüğünü ve doğruluğunu kontrol eder.
    ** 'build().parseSignedClaims(token).getPayload()' -> JWT'nin içeriğini (claims) çıkarır.
    ** 'claimsTFunction.apply' -> çıkarılan 'claims' nesnesine belirtilen fonksiyonu uygular ve sonucu döner.
     */
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    /*
    * Oluşturulan JWT'nin geçerli olup olmadığını kontrol eder. Kullanıcı adının ve token'ın süresinin dolup dolmadığı
    gibi bilgileri kontrol ederek token'ın geçerliliğini doğrular.
    * 'extractUsername(token)' -> token'dan kullanıcı adını çıkarır. Daha önce tanımlanan 'extractUsername' yöntemi,
    token'ın içeriğinden (claims) kullanıcı adını (subject) alır.
    * 'username.equals(userDetails.getUsername())' -> token'dan çıkarılan kullanıcı adı ile 'userDetails' nesnesindeki
    kullanıcı adı eşleşiyor mu diye kontrol eder. Bu token'ın doğru kullanıcıya ait olup olmadığını kontrol eder.
    * '!isTokenExpired(token)' -> token'ın süresinin dolup dolmadığını kontrol eder.
    * 'return (username.equals(userDetails.getUsername()) && !isTokenExpired(token))' -> kullanıcı adları eşleşiyorsa
    ve token süresi dolmamışsa 'true' döner, aksi halde 'false' döner. Bu tokenin geçerli olup olmadığını kontrol eder.
     */
    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /*
    Bu kod, verilen bir JSON Web Token'ın (JWT) süresinin dolup dolmadığını kontrol eder. Token'ın sona erme tarihini
    alarak, bu tarihin şu anki tarihten önce olup olmadığını belirler.
     */
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
