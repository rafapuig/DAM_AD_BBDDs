package dam.ad.stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

public class StreamAdapter<T> implements Stream<T> {

    protected Stream<T> delegateStream;

    protected StreamAdapter(Stream<T> delegateStream) {
        this.delegateStream = delegateStream;
    }

    //protected StreamAdapter(Stream<T> stream) {
    //    delegateStream = StreamSupport.stream(stream.spliterator(), stream.isParallel());
    //}

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return this.delegateStream.filter(predicate).onClose(delegateStream::close);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return delegateStream.map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return delegateStream.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return delegateStream.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return delegateStream.mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return delegateStream.flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return delegateStream.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return delegateStream.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return delegateStream.flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return delegateStream.distinct();
    }

    @Override
    public Stream<T> sorted() {
        return delegateStream.sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return delegateStream.sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return delegateStream.peek(action);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        return delegateStream.limit(maxSize);
    }

    @Override
    public Stream<T> skip(long n) {
        return delegateStream.limit(n);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        System.out.println("StreamAdapter: llamada a foreach");
        delegateStream.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        delegateStream.forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return delegateStream.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return delegateStream.toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return delegateStream.reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return delegateStream.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return delegateStream.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return delegateStream.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return delegateStream.collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return delegateStream.min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return delegateStream.max(comparator);
    }

    @Override
    public long count() {
        return delegateStream.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return delegateStream.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return delegateStream.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return delegateStream.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return delegateStream.findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return delegateStream.findAny();
    }

    @Override
    public Iterator<T> iterator() {
        return delegateStream.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return delegateStream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return delegateStream.isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return delegateStream.sequential();
    }

    @Override
    public Stream<T> parallel() {
        return delegateStream.parallel();
    }

    @Override
    public Stream<T> unordered() {
        return delegateStream.unordered();
    }

    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        return delegateStream.onClose(closeHandler);
    }

    @Override
    public void close() {
        System.out.println("Llamada al autoclose en StreamAdapter");
        delegateStream.close();
    }
}
