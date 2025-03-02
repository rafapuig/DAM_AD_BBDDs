module entity.utils {
    requires static lombok;
    requires java.desktop;
    exports dam.ad.converters;
    exports dam.ad.printers;
    exports dam.ad.dto.annotations;
    exports dam.ad.headers;
    exports dam.ad.consumers;
    exports dam.ad.reflection;
}