package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
    /**
     * the class do load to graph for json
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        DWGraph_DS g = new DWGraph_DS();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();

        for (JsonElement j: nodes){
            int id = j.getAsJsonObject().get("id").getAsInt();
            node_data n = new NodeData(id);
            String [] x_y_z  = j.getAsJsonObject().get("pos").getAsString().split(",");
            geo_location location = new GeoLocation(Double.parseDouble(x_y_z[0]),Double.parseDouble(x_y_z[1]),Double.parseDouble(x_y_z[2]));
            n.setLocation(location);
            g.addNode(n);
        }
        JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
        for (JsonElement j: edges){
            int src = j.getAsJsonObject().get("src").getAsInt();
            int dest = j.getAsJsonObject().get("dest").getAsInt();
            double w = j.getAsJsonObject().get("w").getAsDouble();
            g.connect(src,dest,w);
        }
        return g;

    }
}
