package com.almersal.android.repositories

import com.almersal.android.data.entities.*


class DummyDataRepositories {

    companion object {

        fun getCategoriesList() = mutableListOf<Category>().apply {
            add(BusinessGuideCategory("سيارات", "Cars Cars", "1"))
            add(BusinessGuideCategory("أثاث", "Furnitures Cars", "2"))
            add(BusinessGuideCategory("ألبسة", "Clothes", "3"))
            add(BusinessGuideCategory("عقارات", "Houses", "4"))
            add(BusinessGuideCategory("إلكترونيات", "Electrics", "5"))

            add(BusinessGuideCategory("سيارات", "Cars", "1"))
            add(BusinessGuideCategory("أثاث", "Furnitures", "2"))
            add(BusinessGuideCategory("ألبسة", "Clothes", "3"))
            add(BusinessGuideCategory("عقارات", "Houses", "4"))
            add(BusinessGuideCategory("إلكترونيات", "Electrics", "5"))

            add(BusinessGuideCategory("سيارات", "Cars", "1"))
            add(BusinessGuideCategory("أثاث", "Furnitures", "2"))
            add(BusinessGuideCategory("ألبسة", "Clothes", "3"))
            add(BusinessGuideCategory("عقارات", "Houses", "4"))
            add(BusinessGuideCategory("إلكترونيات", "Electrics", "5"))

        }

        fun getSubCategoriesList() = mutableListOf<SubCategory>().apply {
            add(SubCategory("مارسيديس", "Marcedes", "1"))
            add(SubCategory("أودي", "Audi", "2"))
            add(SubCategory("نيسان", "Nissan", "3"))
            add(SubCategory("شام", "Sham", "4"))
            add(SubCategory("تيوتا", "Toyota", "5"))

            add(SubCategory("مارسيديس", "Marcedes", "1"))
            add(SubCategory("أودي", "Audi", "2"))
            add(SubCategory("نيسان", "Nissan", "3"))
            add(SubCategory("شام", "Sham", "4"))
            add(SubCategory("تيوتا", "Toyota", "5"))

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
            add(LocationEntity("الصالحية", "Homs", "3"))
            add(LocationEntity("باب سريجة", "Hama", "4"))
            add(LocationEntity("المهجرين", "Sywida", "5"))
        }

        fun getTagsDefaultRepositories() = mutableListOf<TagEntity>().apply {
            //            add(TagEntity("دمشق", "Damascus"))
            add(TagEntity("كل الإعلانات", "All ads"))
        }

//        fun getTagsRepositories() = mutableListOf<TagEntity>().apply {
//            add(TagEntity("دمشق", "Damascus"))
//            add(TagEntity("حلب", "Aleppo"))
//            add(TagEntity("الكل", "All"))
//            add(TagEntity("سيارات", "Cars"))
//            add(TagEntity("دمشق شسيسيشسيش", "Damascus"))
//            add(TagEntity("حلب", "Aleppo"))
//            add(TagEntity("الكل", "All"))
//            add(TagEntity("سيارات", "Cars"))
//        }

        fun getMediaList() = mutableListOf<MediaEntity>().apply {
            add(MediaEntity("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg"))
            add(MediaEntity("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg"))
            add(MediaEntity("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg"))
        }

        fun getPostList() = mutableListOf<Post>().apply {
            add(Post("mercedes E350", "نضيفة خالية برا جوا زنار نضافة", "activated",
                    "https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg"
                    , PostCategory("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("مرسيدس", "mercedes", "1"),
                    City("دمشق", "Damascus", "1"),
                    LocationEntity("البرامكة", "Baramka", "1")
            ))
            add(Post("iphone X", "bla bla bla bla", "activated",
                    ""
                    , PostCategory("هواتف للبيع", "phones for sale", "1"),
                    SubCategory("iphone", "iphone", "1"),
                    City("حلب", "Aleppo", "2"),
                    LocationEntity("الشاغور", "Al Shaggor", "2")
            ))
            add(Post("BMW x5", "bla bla bla bla", "activated",
                    "https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg"
                    , PostCategory("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("BMW", "BMW", "1"),
                    City("حمص", "Homs", "3"),
                    LocationEntity("الصالحية", "Al Salheh", "3")
            ))
            add(Post("iphone X", "bla bla bla bla", "activated",
                    "https://images.pexels.com/photos/3510/hand-apple-iphone-smartphone.jpg?cs=srgb&dl=apple-apps-hand-3510.jpg&fm=jpg"
                    , PostCategory("هواتف للبيع", "phones for sale", "1"),
                    SubCategory("iphone", "iphone", "1"),
                    City("حماة", "Hama", "4"),
                    LocationEntity("باب سريجة", "Bab Sreja", "4")
            ))
            add(Post("BMW x5", "bla bla bla bla", "activated",
                    ""
                    , PostCategory("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("BMW", "BMW", "1"),
                    City("السويداء", "Sywida", "5"),
                    LocationEntity("المهجرين", "Al mohajreen", "5")
            ))
            add(Post("mercedes E350", "نضيفة خالية برا جوا زنار نضافة", "activated",
                    "https://images.pexels.com/photos/3510/hand-apple-iphone-smartphone.jpg?cs=srgb&dl=apple-apps-hand-3510.jpg&fm=jpg"
                    , PostCategory("سيارات للبيع", "cars for sale", "1"),
                    SubCategory("مرسيدس", "mercedes", "1"),
                    City("السويداء", "Sywida", "5"),
                    LocationEntity("المهجرين", "Al mohajreen", "5")
            ))
        }

        fun getBusinessGuideList() = mutableListOf<BusinessGuide>().apply {
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
            add(BusinessGuide("https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?cs=srgb&dl=automobile-cars-headlights-120049.jpg&fm=jpg",
                    "Honda Car", "Honda Car", BusinessGuideCategory("سيارات", "Cars", "1"), SubCategory("مارسيديس", "Marcedes", "1")))
        }

        fun getAttachmentList() = mutableListOf<Attachment>().apply {
            //            add(Attachment())
//            add(Attachment())
//            add(Attachment())
//            add(Attachment())
//            add(Attachment())
        }

    }

}