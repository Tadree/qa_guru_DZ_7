package ru.jiehk;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VprokTests {
    @ValueSource(strings = {"Овощи, фрукты, ягоды", "Мясо, птица, колбасы", "Рыба, икра "})
    @ParameterizedTest(name = "Проверка перехода из бургер меню в раздел каталога {0}")
    void redirectToCategoryPageFromCatalogTest(String catalogCategoryName) {
        open("https://www.vprok.ru/");
        $(".Burger_burger__bNSA_").click();
        $$(".CatalogMenuLink_parentLink__5IG3T").find(text(catalogCategoryName)).click();
        $(".TitlePage_title__YjxvC").shouldHave(text(catalogCategoryName));
    }

    @CsvSource(value = {
            "Скидка 10% на первые три заказа, -10% по промокоду WELCOME",
            "Скидки на мясные продукты, Мясо и мясные продукты со скидками",
            "Скидки только на Vprok.ru, Эксклюзивные скидки недели!"
    })
    @ParameterizedTest(name = "Проверка перехода с главного баннера на промостраницу {0}")
    void redirectToPromoPageFromMainBannerTest(String mainBannerPromoName, String promoPageTitle) {
        open("https://www.vprok.ru/");
        $$(".ListItem_button__ZvJ1h").find(text(mainBannerPromoName)).click();
        $(".Carousel_wrapper__ac_ZZ").click();
        $(".xf-promo-detail__title").shouldHave(text(promoPageTitle));
    }

    static Stream<Arguments> catalogSubCategoryListTest() {
        return Stream.of(
                Arguments.of("Готовая еда", List.of("Салаты и закуски", "Второе", "Сэндвичи и выпечка",
                        "Полуфабрикаты", "Готовые завтраки", "Семейный формат", "Суши и роллы", "Супы",
                        "Напитки и фруктовые нарезки", "Свежий хлеб", "Готовые замороженные блюда", "Наше производство")),
                Arguments.of("Мясо, птица, колбасы", List.of("Мясо", "Наше производство", "Птица", "Стейки",
                        "Деликатесы и колбасные изделия", "Печень, сердце, желудок, субпродукты")),
                Arguments.of("Чипсы, снеки, орехи", List.of("Чипсы", "Lay's", "Овощные, фруктовые и злаковые снеки",
                        "Попкорн", "Кукурузные снеки", "Семечки, сухофрукты", "Сухарики", "Орехи"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка отображения в каталоге подкатегорий для категории {0}")
    void catalogSubCategoryListTest(String catalogCategoryName, List<String> subCategoriesNames) {
        open("https://www.vprok.ru/");
        $(".Burger_burger__bNSA_").click();
        $$(".CatalogMenuLink_parentLink__5IG3T").find(text(catalogCategoryName)).hover();
        $$(".CategoryList_headerLink__8BIfP").filter(visible).shouldHave(CollectionCondition.texts(subCategoriesNames));
    }
}
