package com.khoadonguyen.java_music_streaming.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomSlug {
    private static final List<String> queries = Arrays.asList(
            "Taylor Swift Love Story official",
            "BLACKPINK DDU-DU DDU-DU mv",
            "Adele Hello official music video",
            "Ed Sheeran Shape of You official",
            "Charlie Puth Attention official",
            "Billie Eilish Bad Guy mv",
            "Son Tung M-TP Hay Trao Cho Anh",
            "IU Celebrity official mv",
            "Bruno Mars That's What I Like",
            "Travis Scott Goosebumps official",
            "Rihanna Diamonds mv",
            "Maroon 5 Sugar official",
            "Lady Gaga Shallow",
            "Katy Perry Roar official video",
            "Lana Del Rey Summertime Sadness",
            "BTS Dynamite official mv",
            "Lizzo About Damn Time official",
            "Doja Cat Say So mv",
            "Justin Bieber Sorry mv",
            "Alan Walker Faded",
            "Troye Sivan Youth official",
            "Post Malone Circles official",
            "Drake God's Plan official",
            "Vũ Đông Kiếm Em",
            "Hoàng Dũng Nàng Thơ official",
            "AMEE Anh Nhà Ở Đâu Thế",
            "Min Có Em Chờ official",
            "Đen Vâu Trốn Tìm mv",
            "Rhymastic Yêu 5 official",
            "Binz OK official",
            "SOOBIN Tháng Năm official mv",
            "Hòa Minzy Không Thể Cùng Nhau Suốt Kiếp",
            "Bích Phương Một Cú Lừa official"
    );


    private static final Random random = new Random();

    public static String gSlugs() {
        return queries.get(random.nextInt(queries.size()));
    }

    public static List<String> getAllSlugs() {
        return queries;
    }


}
