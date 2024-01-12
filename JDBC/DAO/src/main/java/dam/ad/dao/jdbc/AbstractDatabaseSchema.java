package dam.ad.dao.jdbc;

import java.net.URL;

public abstract class AbstractDatabaseSchema implements DatabaseSchema {
    String sqlCreateSchema;
    protected AbstractDatabaseSchema() {
        initCreateSchemaScript();
    }

    protected abstract String getResourceName();

    private void initCreateSchemaScript() {
        URL url = this.getClass().getResource(getResourceName());
        this.sqlCreateSchema = DatabaseSchema.super.getCreateSchemaFrom(url);
    }

    @Override
    public String getCreateSchema() {
        return this.sqlCreateSchema;
    }

    @Override
    public abstract String getDropSchema();


}
