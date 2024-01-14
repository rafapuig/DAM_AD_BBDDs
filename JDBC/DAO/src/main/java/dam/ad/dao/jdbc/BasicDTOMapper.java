package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.stream.Failure;
import dam.ad.stream.Success;
import dam.ad.stream.Try;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicDTOMapper implements DTOMapper<List<Object>> {
    @Override
    public List<Object> apply(ResultSet resultSet) throws SQLException {

        return IntStream.rangeClosed(1, resultSet.getMetaData().getColumnCount())
                .mapToObj(index -> Try.of(()-> resultSet.getObject(index)))
                .map(objectTry -> switch (objectTry) {
                    case Success(Object result) -> result;
                    case Failure(Throwable throwable) -> Optional.empty();
                })
                .collect(Collectors.toList());

        /*List<Object> list = new ArrayList<>();
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            list.add(resultSet.getObject(i+1));
        }
        return list;*/
    }

    private static BasicDTOMapper singleton;

    public static BasicDTOMapper getInstance() {
        if(singleton == null) {
            singleton = new BasicDTOMapper();
        }
        return singleton;
    }
}
