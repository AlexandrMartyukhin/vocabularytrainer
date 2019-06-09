package ru.minilan.vocabularytrainer;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslate {

    @POST("api/v1.5/tr.json/translate")
    Call<Translate> translate(@Query("lang") String lang, @Query("text") String word, @Query("key") String keyApi);

}
