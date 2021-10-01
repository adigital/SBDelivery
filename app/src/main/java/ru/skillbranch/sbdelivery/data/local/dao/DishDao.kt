package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entities.Dish

@Dao
interface DishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dishes: List<Dish>)

    @Query("SELECT * FROM Dishes")
    fun getDishes(): LiveData<List<Dish>>

//    @Query("SELECT * FROM Dishes WHERE recommended = 1 ORDER BY RANDOM()")
//    fun getRecommendedDishes(): List<Dish>

    @Query("SELECT * FROM Dishes WHERE recommended = 1")
    fun getRecommendedDishes(): LiveData<List<Dish>>

//    @Query("SELECT * FROM Dishes WHERE rating > 4.8 ORDER BY RANDOM() LIMIT 10")
//    fun getBestDishes(): List<Dish>

    @Query("SELECT * FROM Dishes WHERE rating >= 4.8")
    fun getBestDishes(): LiveData<List<Dish>>

//    @Query(
//        "SELECT * FROM (\n" +
//                "\tSELECT * FROM (\n" +
//                "\t\tSELECT * FROM Dishes WHERE likes > 0 ORDER BY likes DESC LIMIT 10\n" +
//                "\t)\n" +
//                "\tUNION\n" +
//                "\tSELECT * FROM (\t\n" +
//                "\t\tSELECT * FROM Dishes where likes = 0 ORDER BY RANDOM() DESC LIMIT 10\n" +
//                "\t)\n" +
//                "\tLIMIT 10\n" +
//                ")\n" +
//                "ORDER BY RANDOM() DESC"
//    )
//    fun getLikesDishes(): List<Dish>

    @Query("SELECT * FROM Dishes WHERE likes > 0 ORDER BY likes DESC LIMIT 10")
    fun getLikesDishes(): LiveData<List<Dish>>

    @Query("UPDATE Dishes SET recommended = 0")
    fun resetRecommendedDishes()

    @Query("UPDATE Dishes SET recommended = 1 WHERE id IN (:ids)")
    fun setRecommendedDishes(ids: List<String>)


    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY name")
    fun getDishesInCategoryByName(category: String?): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY name DESC")
    fun getDishesInCategoryByNameDesc(category: String?): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY likes")
    fun getDishesInCategoryByLikes(category: String?): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY likes DESC")
    fun getDishesInCategoryByLikesDesc(category: String?): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY rating")
    fun getDishesInCategoryByRating(category: String?): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE category = :category ORDER BY rating DESC")
    fun getDishesInCategoryByRatingDesc(category: String?): LiveData<List<Dish>>


    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY name")
    fun getActionDishesByName(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY name DESC")
    fun getActionDishesByNameDesc(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY likes")
    fun getActionDishesByLikes(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY likes DESC")
    fun getActionDishesByLikesDesc(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY rating")
    fun getActionDishesByRating(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE oldPrice <> 0 ORDER BY rating DESC")
    fun getActionDishesByRatingDesc(): LiveData<List<Dish>>


    @Query("SELECT * FROM Dishes WHERE name LIKE :query ORDER BY name")
    fun getSearchResultDishes(query: String): LiveData<List<Dish>>


    @Query("UPDATE Dishes SET favorite = :favorite  WHERE id IN (:ids)")
    fun setFavoriteDishes(ids: List<String>, favorite: Boolean)

    @Query("UPDATE Dishes SET favorite = :favorite  WHERE id = :id")
    fun setFavoriteDish(id: String, favorite: Boolean)

    @Query("SELECT * FROM Dishes WHERE favorite = 1 ORDER BY name")
    fun getFavoriteDishes(): LiveData<List<Dish>>

    @Query("SELECT * FROM Dishes WHERE favorite = 1 ORDER BY name")
    fun getFavoriteDishesList(): List<Dish>

    @Query("SELECT count(*) FROM Dishes WHERE favorite = 1")
    fun getFavoriteDishesCount(): Int

    @Query("UPDATE Dishes SET favorite = 0")
    fun clearAllFavoriteDishes()


    @Query("SELECT count(*) FROM Dishes WHERE oldPrice <> 0")
    fun getActionDishesCount(): Int

    @Query("SELECT * FROM Dishes WHERE id = :id LIMIT 1")
    fun getDish(id: String): LiveData<Dish>

}