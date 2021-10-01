package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.FavoriteChangeDishReq
import ru.skillbranch.sbdelivery.data.remote.res.DishRes
import ru.skillbranch.sbdelivery.data.remote.res.FavoriteDishRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import java.util.*

object DishesRepository {

    private var dishesDao = DbManager.db.dishesDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager

    private val seed = System.currentTimeMillis()

    suspend fun insertDishesDao(dishes: List<Dish>) {
        dishesDao.insertDish(dishes)
    }

    fun getRecommendedDishesDao() =
        Transformations.map(dishesDao.getRecommendedDishes()) {
            it.shuffled(
                Random(
                    seed
                )
            )
        }

    fun setRecommendedDishesDao(ids: List<String>) {
        dishesDao.setRecommendedDishes(ids)
    }

    fun resetRecommendedDishes() {
        dishesDao.resetRecommendedDishes()
    }


    fun getBestDishesDao(): LiveData<List<Dish>> =
        Transformations.map(dishesDao.getBestDishes()) {
            var bestDishes = mutableListOf<Dish>()
            bestDishes.addAll(it)
            bestDishes = bestDishes.shuffled(Random(seed)).toMutableList()

            if (bestDishes.size > 10) {
                bestDishes = bestDishes.subList(0, 9)
            }

            return@map bestDishes.toList()
        }


    fun getLikesDishesDao(): LiveData<List<Dish>> =
        Transformations.map(dishesDao.getDishes()) { it ->
            var likesDishes = mutableListOf<Dish>()
            likesDishes.addAll(it.filter { it.likes > 0 })

            if (likesDishes.size in 1..9) {
                var noLikesDishes = mutableListOf<Dish>()
                noLikesDishes.addAll(it.filter { it.likes == 0 })

                if (noLikesDishes.size > 0) {
                    noLikesDishes = noLikesDishes.shuffled(Random(seed)).toMutableList()

                    for ((n, _) in (likesDishes.size..10).withIndex()) {
                        likesDishes.add(noLikesDishes[n])
                    }
                }
            }

            likesDishes = likesDishes.shuffled(Random(seed)).toMutableList()

            return@map likesDishes.toList()
        }


    fun getDishesInCategoryByNameDao(category: String?) =
        dishesDao.getDishesInCategoryByName(category)

    fun getDishesInCategoryByNameDescDao(category: String?) =
        dishesDao.getDishesInCategoryByNameDesc(category)

    fun getDishesInCategoryByLikesDao(category: String?) =
        dishesDao.getDishesInCategoryByLikes(category)

    fun getDishesInCategoryByLikesDescDao(category: String?) =
        dishesDao.getDishesInCategoryByLikesDesc(category)

    fun getDishesInCategoryByRatingDao(category: String?) =
        dishesDao.getDishesInCategoryByRating(category)

    fun getDishesInCategoryByRatingDescDao(category: String?) =
        dishesDao.getDishesInCategoryByRatingDesc(category)


    fun getActionDishesByNameDao() = dishesDao.getActionDishesByName()

    fun getActionDishesByNameDescDao() = dishesDao.getActionDishesByNameDesc()

    fun getActionDishesByLikesDao() = dishesDao.getActionDishesByLikes()

    fun getActionDishesByLikesDescDao() = dishesDao.getActionDishesByLikesDesc()

    fun getActionDishesByRatingDao() = dishesDao.getActionDishesByRating()

    fun getActionDishesByRatingDescDao() = dishesDao.getActionDishesByRatingDesc()


    fun getSearchResultDishesDao(query: String) = dishesDao.getSearchResultDishes("%$query%")


    fun updateFavoriteDishDao(id: String, favorite: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            dishesDao.setFavoriteDish(id, favorite)
        }
    }

    fun setFavoriteDishesDao(ids: List<String>, favorite: Boolean) {
        dishesDao.setFavoriteDishes(ids, favorite)
    }

    fun getFavoriteDishesDao() = dishesDao.getFavoriteDishes()

    fun getFavoriteDishesList() = dishesDao.getFavoriteDishesList()

    fun getFavoriteDishesCountDao() = dishesDao.getFavoriteDishesCount()


    fun getDishDao(id: String) = dishesDao.getDish(id)

    fun getActionDishesCountDao(): Int = dishesDao.getActionDishesCount()

    suspend fun getDishesNet(): List<DishRes> {
        val dishes = mutableListOf<DishRes>()
        var offset = 0
        var expires: String? = null

        try {
            while (true) {
                val res = network.getDishes(offset * 10, 10, preferences.dishesExpires)
                if (res.isSuccessful) {
                    offset++
                    dishes.addAll(res.body()!!)

                    expires = res.headers()["Expires"]
                } else break
            }

            expires?.let { preferences.dishesExpires = it }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return dishes
    }

    suspend fun getRecommendedNet(): List<String> {
        var recommend = listOf<String>()

        try {
            val res = network.getRecommended(preferences.recommendExpires)
            if (res.isSuccessful) {
                recommend = res.body()!!
                res.headers()["Expires"]?.let { preferences.recommendExpires = it }
            }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return recommend
    }

    suspend fun getFavoriteDishesNet(): List<FavoriteDishRes> {
        val favoriteDishes = mutableListOf<FavoriteDishRes>()
        var offset = 0
        var expires: String? = null

        try {
            while (true) {
                val res = network.getFavorite(
                    offset * 10,
                    10,
//                    preferences.favoriteExpires,
                    token = preferences.accessToken
                )
                if (res.isSuccessful) {
                    offset++
                    favoriteDishes.addAll(res.body()!!)

                    expires = res.headers()["Expires"]
                } else break
            }

            expires?.let { preferences.favoriteExpires = it }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return favoriteDishes
    }

    fun changeFavoriteDish(id: String, favorite: Boolean) {
        val favoriteDishChange = FavoriteChangeDishReq(id, favorite)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                network.changeFavorite(listOf(favoriteDishChange), token = preferences.accessToken)
                dishesDao.setFavoriteDish(id, favorite)
            } catch (e: Throwable) {
                if (e.message != "End of input") {
                    notifyMainShort(e.localizedMessage)
                }
            }
        }
    }

    suspend fun changeFavoriteDishesNet(ids: List<String>, favorite: Boolean) {
        val favoriteDishesChange = mutableListOf<FavoriteChangeDishReq>()

        ids.forEach {
            favoriteDishesChange.add(FavoriteChangeDishReq(it, favorite))
        }

        try {
            network.changeFavorite(favoriteDishesChange, token = preferences.accessToken)
        } catch (e: Throwable) {
            if (e.message != "End of input") {
                notifyMainShort(e.localizedMessage)
            }
        }
    }
}