package ru.skillbranch.sbdelivery.data

import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.local.entities.Review
import ru.skillbranch.sbdelivery.data.local.entities.Status
import ru.skillbranch.sbdelivery.data.remote.res.CategoryRes
import ru.skillbranch.sbdelivery.data.remote.res.DishRes
import ru.skillbranch.sbdelivery.data.remote.res.ReviewsRes
import ru.skillbranch.sbdelivery.data.remote.res.StatusRes


fun List<DishRes>.toDishesList(): List<Dish> {
    val dishes = mutableListOf<Dish>()

    forEach { dishRes ->
        dishes.add(
            Dish(
                id = dishRes.id,
                name = dishRes.name,
                description = dishRes.description,
                image = dishRes.image,
                oldPrice = dishRes.oldPrice,
                price = dishRes.price,
                rating = dishRes.rating,
                commentsCount = dishRes.commentsCount,
                likes = dishRes.likes,
                category = dishRes.category,
                active = dishRes.active,
                createdAt = dishRes.createdAt,
                updatedAt = dishRes.updatedAt,
            )
        )
    }

    return dishes
}

fun List<CategoryRes>.toCategoriesList(): List<Category> {
    val categories = mutableListOf<Category>()

    forEach { categoryRes ->
        categories.add(
            Category(
                categoryId = categoryRes.categoryId,
                name = categoryRes.name,
                order = categoryRes.order,
                icon = categoryRes.icon,
                parent = categoryRes.parent,
                active = categoryRes.active,
                createdAt = categoryRes.createdAt,
                updatedAt = categoryRes.updatedAt,
            )
        )
    }

    return categories
}

fun List<ReviewsRes>.toReviewsList(): List<Review> {
    val reviews = mutableListOf<Review>()

    forEach { reviewsRes ->
        reviews.add(
            Review(
                dishId = reviewsRes.dishId,
                author = reviewsRes.author,
                date = reviewsRes.date,
                order = reviewsRes.order,
                rating = reviewsRes.rating,
                text = reviewsRes.text,
                active = reviewsRes.active,
                createdAt = reviewsRes.createdAt,
                updatedAt = reviewsRes.updatedAt
            )
        )
    }

    return reviews
}

fun List<StatusRes>.toStatusesList(): List<Status> {
    val statuses = mutableListOf<Status>()

    forEach { status ->
        statuses.add(
            Status(
                status.id,
                status.name,
                status.cancelable,
                status.active,
                status.createdAt,
                status.updatedAt
            )
        )
    }

    return statuses
}