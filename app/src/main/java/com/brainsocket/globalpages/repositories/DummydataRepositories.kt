package com.brainsocket.globalpages.repositories

import com.brainsocket.globalpages.data.entities.*

/**
 * Created by Adhamkh on 2018-07-03.
 */
class DummydataRepositories {
    companion object {

        fun getCategoiesList() = mutableListOf<Category>().apply {
            add(Category("سيارات", "Cars", "1"))
            add(Category("أثاث", "Furnitures", "2"))
            add(Category("ألبسة", "Clothes", "3"))
            add(Category("حماة", "Hama", "4"))
            add(Category("السويداء", "Sywida", "5"))
        }

        fun getSubCategoriesList() = mutableListOf<SubCategory>().apply {
            add(SubCategory("مارسيديس", "Marcedes", "1"))
            add(SubCategory("أودي", "Audi", "2"))
            add(SubCategory("نيسان", "Nissan", "3"))
            add(SubCategory("شام", "Sham", "4"))
            add(SubCategory("تيوتا", "Toyota", "5"))
        }

        fun getCityList() = mutableListOf<City>().apply {
            add(City("دمشق", "Damascus", "1"))
            add(City("حلب", "Aleppo", "2"))
            add(City("حمص", "Homs", "3"))
            add(City("حماة", "Hama", "4"))
            add(City("السويداء", "Sywida", "5"))
        }

        fun getLocationList() = mutableListOf<LocationEntity>().apply {
            add(LocationEntity("البرامكة", "Baramka", "1"))
            add(LocationEntity("الشاغور", "Aleppo", "2"))
            add(LocationEntity("الصالجية", "Homs", "3"))
            add(LocationEntity("باب سريجة", "Hama", "4"))
            add(LocationEntity("المهجرين", "Sywida", "5"))
        }

        fun getTagsRepositories() = mutableListOf<TagEntity>().apply {
            add(TagEntity("دمشق شسيسيشسيش", "Damascus"))
            add(TagEntity("حلب", "Aleppo"))
            add(TagEntity("الكل", "All"))
            add(TagEntity("سيارات", "Cars"))
            add(TagEntity("دمشق شسيسيشسيش", "Damascus"))
            add(TagEntity("حلب", "Aleppo"))
            add(TagEntity("الكل", "All"))
            add(TagEntity("سيارات", "Cars"))
        }

        fun getMediaList() = mutableListOf<MediaEntity>().apply {
            add(MediaEntity("https://pixabay.com/get/e83db6062ef7013ed1584d05fb1d4f92e074ead019ac104496f0c670afecb0b0_1280.jpg"))
            add(MediaEntity("https://pixabay.com/get/e83db80f2cfd053ed1584d05fb1d4f92e074ead019ac104496f0c67ea1ebb5b0_1280.jpg"))
            add(MediaEntity("https://pixabay.com/get/eb33b40e2bfd023ed1584d05fb1d4f92e074ead019ac104496f0c670afecb0b0_1280.jpg"))
        }

        fun getPostList() = mutableListOf<Post>().apply {

            add(Post("mercedes E350", "نضيفة خالية برا جوا زنار نضافة", "activated",
                    "https://pixabay.com/get/e135b00621f01c22d2524518b7454295e07fe7d504b0144295f8c179a2ebb3_1280.jpg"
                    , Category("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("مرسيدس", "mercedes", "1")))
            add(Post("iphone X", "bla bla bla bla", "activated",
                    ""
                    , Category("هواتف للبيع", "phones for sale", "1"),
                    SubCategory("iphone", "iphone", "1")))

            add(Post("BMW x5", "bla bla bla bla", "activated",
                    "https://pixabay.com/get/e83db80f2cfd053ed1584d05fb1d4f92e074ead019ac104496f0c67ea1ebb5b0_1280.jpg"
                    , Category("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("BMW", "BMW", "1")))

            add(Post("iphone X", "bla bla bla bla", "activated",
                    "https://pixabay.com/get/e83db80f2cfd053ed1584d05fb1d4f92e074ead019ac104496f0c67ea1ebb5b0_1280.jpg"
                    , Category("هواتف للبيع", "phones for sale", "1"),
                    SubCategory("iphone", "iphone", "1")))
            add(Post("BMW x5", "bla bla bla bla", "activated",
                    ""
                    , Category("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("BMW", "BMW", "1")))
            add(Post("mercedes E350", "نضيفة خالية برا جوا زنار نضافة", "activated",
                    "https://pixabay.com/get/e135b00621f01c22d2524518b7454295e07fe7d504b0144295f8c179a2ebb3_1280.jpg"
                    , Category("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("مرسيدس", "mercedes", "1")))

        }


    }
}