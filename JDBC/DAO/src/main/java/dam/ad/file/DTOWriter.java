package dam.ad.file;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public abstract class DTOWriter<T> {
    protected abstract URL getURL();

    public void write(T dto) {
        String target = getURL().getFile();
        try (OutputStream os = new FileOutputStream(target, true)) {
            PrintWriter pw = new PrintWriter(os);
            pw.println(toCSV(dto));
            pw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void replace(T dto) {
        Path source = Path.of(getURL().getFile());

    }

    public void replace(T oldDTO, T newDTO) {
        Path source = Path.of(getURL().getFile());

        try (Stream<String> lines = Files.lines(source)) {
            List<String> list = lines
                    .map(line -> toCSV(oldDTO).equals(line) ?
                            toCSV(newDTO) : line).toList();

            Files.write(source, list, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract String toCSV(T dto);
}
