package javaClasses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий с "Очками"
 */
@Repository
interface GlassesRepository extends JpaRepository<Glasses, Long> {
    /**
     * Поиск элементов, значение диоптрий
     * которых выше введённого порога и где элементы не куплены
     * @param diopters порог диоптрий
     * @return список с найденными элементами
     */
    List<Glasses> findGlassesByDioptersGreaterThanAndPurchasedIsFalse(double diopters);

    /**
     * Вывести все элементы, отсортированные по id и где элементы не куплены
     * @return отсортированный список
     */
    List<Glasses> findAllByPurchasedIsFalseOrderById();

    /**
     * Вывести все элемент по id  и где элементы не куплены
     * @return элемент c id
     */
    Optional<Glasses> findByIdAndPurchasedIsFalse(Long id);

    /**
     * Вывести последний созданный элемент
     * @return последний созданный элемент
     */
    Glasses findFirstByOrderByIdDesc();
}