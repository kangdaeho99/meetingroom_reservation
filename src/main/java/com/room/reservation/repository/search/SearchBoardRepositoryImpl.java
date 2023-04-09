package com.room.reservation.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.room.reservation.entity.Board;
import com.room.reservation.entity.QBoard;
import com.room.reservation.entity.QMember;
import com.room.reservation.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description :
 * Spring Data JPA의 Repository를 확장하기 위해 다음과 같은 단계로 처리
 * - 쿼리메서드나 @Query 등으로 처리할 수 없는 기능은 별도의 인터페이스로 설계
 * - 별도의 인터페이스에 대한 구현 클래스를 작성. 이때 QueryDslRepositorySupport라는 클래스를 부모 클래스로 사용
 * - 구현 클래스에 인터페이스의 기능을 Q도메인 클래스와 JPQLQuery를 이용해서 구현
 *
 * 개발자가 원하는대로 동작하는 Repository를 작성하는데 있어서 가장 중요한 클래스는 QuerydslRepositorySupport 클래스입니다.
 * QuerydslRepositorySupport클래스는 Spring Data JPA에 포함된 클래스로 Querydsl 라이브러리를 이용해서 직접 무언가를 구현할때 사용
 * 해당 클래스를 사용하고자 할려면 상속합니다.
 * 해당 클래스는 생성자가 존재하므로 super()를 이용해서 호출합니다.
 * 이떄 도메인 클래스를 지정하는데 null값을 넣을 수 없습니다.
 * 또한 BoardRepository는 선언한 SearchBoardRepository 인터페이스를 상속하는 형태로 변경합니다.
 *
 */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl(){
        super(Board.class);
    }

    /**
     * Description :
     *
     * Querydsl 라이브러리 내에는 JPQLQuery 라는 인터페이스를 활용합니다.
     *
     * JPQLQuery의 leftJoin() / on()
     * JPQLQuery로 다른 엔티티와 조인을 처리하기 위해서는 join() 혹은 leftJoin(), rightJoin() 등을 이용하고 필요한경우 on()을 이용해서
     * 조인에 필요한 부분을 완성할 수 있습니다.
     * Board는 Reply와 left (outer) join을 이용해야 하는 상황이면 다음과 같이 코드를 작성합니다.
     * QBoard board = QBoard.board;
     * QReply reply = QReply.reply;
     *
     * JPQLQuery<Board> jpqlQuery = from(board);
     * jpqlQeury.leftJoin(reply).on(reply.board.eq(board));
     *
     * 위와 같이 수정된 search1()의 실행결과에는 아래와 같은 JPQL이 출력됩니다.
     * (JPA 2.1 부터는 left out join 처리에 'on'구문이 추가될 수 있지만, 과거에는 'with'를 사용했습니다.)
     * select board from Board board left join Reply reply with reply.board = board where board.bno = ?1
     *
     * 실행되는 sql은 아래와 같이 'left outer join' 구문이 추가됩니다. 일단 pass
     *
     * Tuple 객체
     * JPQLQuery의 leftJoin(), join()을 이용해서 Board, Member, Reply를 처리할 수 있고,
     * groupBy() 등을 이용해서 집합함수를 처리합니다.
     * 변경된 부분은 Member에 대한 leftjoin()과 select() 뒤의 groupBy()를 적용한 부분입니다.
     * select() 내에도 여러 객체를 가져오는 형태로 변경되엇습니다.
     * 이렇게 정해진 엔티티 객체 단위가 아니라 각각의 데이터를 추출하는 경우에는 Tuple이라는 객체를 이용합니다.
     * 위의 코드를 Tuiple을 이용하도록 수정합니다.
     *
     *
     *
     * @return
     */
    @Override
    public Board search1() {
        log.info("search1 ------------------");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
//        jpqlQuery.select(board).where(board.bno.eq(1L));
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

//        jpqlQuery.select(board, member.email, reply.count()).groupBy(board);
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("------------------");
        log.info(tuple);
//        log.info(jpqlQuery);
        log.info("------------------");

//        List<Board> result = jpqlQuery.fetch();
        List<Tuple> result = tuple.fetch();
        log.info(result);
        return null;
    }

    /**
     *
     * Description :
     * booleanBuilder와 booleanExpression 을 통해 파라미터에 따라 검색조건을 추가합니다.
     * tuple.groupBy() 의 경우 메서드의 하단에서 처리합니다.
     * 남은 작업은 orderBy() 와 Page 타입의 객체를 구성합니다.
     *
     */
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.............................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t) {
                    case "t" -> conditionBuilder.or(board.title.contains(keyword));
                    case "w" -> conditionBuilder.or(member.email.contains(keyword));
                    case "c" -> conditionBuilder.or(board.content.contains(keyword));
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        //order by
//        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

//        sort.stream().forEach(order -> {
//            Order direction = order.isAscending()? Order.ASC: Order.DESC;
//            String prop = order.getProperty();
//
//            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
//            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
//
//        });
//        tuple.groupBy(board);
//
//        //page 처리
//        tuple.offset(pageable.getOffset());
//        tuple.limit(pageable.getPageSize());

        tuple.groupBy(board);

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, tuple);

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count);
    }
}
