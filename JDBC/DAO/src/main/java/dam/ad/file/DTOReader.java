package dam.ad.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class DTOReader<T> {

    public Stream<T> readFromResource(String name) {
        try {
            InputStream is = Objects.requireNonNull(
                            DTOReader.class.
                            getResource(name))
                    .openStream();

            return read(is);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<T> read(InputStream input) {
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(isr);

        return br.lines()
                .map(this::read)
                .onClose(() -> {
                    try {
                        input.close();
                        System.out.println("Closed input stream");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    protected abstract T read(String line);
}
