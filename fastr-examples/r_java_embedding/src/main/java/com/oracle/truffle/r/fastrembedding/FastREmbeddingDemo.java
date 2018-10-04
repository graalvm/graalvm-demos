package com.oracle.truffle.r.fastrembedding;

import java.util.function.Function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

public class FastREmbeddingDemo {

    public static final class User {
        public final int id;
        public final String name;
        public final String favoriteLanguage;

        public User(int id, String name, String favoriteLanguage) {
            this.id = id;
            this.name = name;
            this.favoriteLanguage = favoriteLanguage;
        }
    }

    public static class NameColumn implements ProxyArray {
        private final User[] users;

        public NameColumn(User[] users) {
            this.users = users;
        }

        public Object get(long index) {
            return users[(int) index].name;
        }

        public void set(long index, Value value) {
            throw new UnsupportedOperationException();
        }

        public long getSize() {
            return users.length;
        }
    }

    public static class IdColumn implements ProxyArray {
        private final User[] users;

        public IdColumn(User[] users) {
            this.users = users;
        }

        public Object get(long index) {
            return (Object) users[(int) index].id;
        }

        public void set(long index, Value value) {
            throw new UnsupportedOperationException();
        }

        public long getSize() {
            return users.length;
        }
    }

    public static class AnyColumn implements ProxyArray {
        private final User[] users;
        private final Function<User, Object> getter;

        public AnyColumn(User[] users, Function<User, Object> getter) {
            this.users = users;
            this.getter = getter;
        }


        @Override
        public Object get(long index) {
            return getter.apply(users[(int) index]);
        }

        @Override
        public void set(long index, Value value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getSize() {
            return users.length;
        }
    }

    public static class UsersTable {
        public final IdColumn id;
        public final NameColumn name;
        public final AnyColumn language;

        public UsersTable(User[] users) {
            this.id = new IdColumn(users);
            this.name = new NameColumn(users);
            this.language = new AnyColumn(users, x -> x.favoriteLanguage);
        }
    }

    public static void main(String[] args) {
        Context context = Context.newBuilder("R").allowAllAccess(true).build();
        Value rFunction = context.eval("R",
                "function(table) { " +
                "  table <- as.data.frame(table);" +
                "  cat('The whole data frame printed in R:\n');" +
                "  print(table);" +
                "  cat('---------\n\n');" +

                "  cat('Filter out users with ID>2:\n');" +
                "  print(table[table$id > 2,]);" +
                "  cat('---------\n\n');" +

                "  cat('How many users like Java: ');" +
                "  cat(nrow(table[table$language == 'Java',]), '\n');" +
                "}");
        User[] data = getUsers();
        rFunction.execute(new UsersTable(data));
    }

    private static User[] getUsers() {
        return new User[] {
                new User(1, "Florian", "Python"),
                new User(2, "Lukas", "R"),
                new User(3, "Mila", "Java"),
                new User(4, "Paley", "Coq"),
                new User(5, "Stepan", "C#"),
                new User(6, "Tomas", "Java"),
                new User(7, "Zbynek", "Scala"),
        };
    }
}
