module personas.model {
    requires entity.utils;
    exports dam.ad.model.personas;
    opens dam.ad.model.personas to entity.utils;
}