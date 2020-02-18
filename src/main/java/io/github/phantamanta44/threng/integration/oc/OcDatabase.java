package io.github.phantamanta44.threng.integration.oc;

import li.cil.oc.api.internal.Database;
import li.cil.oc.api.network.Component;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Node;

public class OcDatabase {

    public static Database getDatabase(Node node, String address) {
        Node dbNode = node.network().node(address);
        if (dbNode instanceof Component) {
            Environment db = dbNode.host();
            if (db instanceof Database) {
                return (Database)db;
            } else {
                throw new IllegalArgumentException("not a database");
            }
        } else {
            throw new IllegalArgumentException("no such component");
        }
    }

}
