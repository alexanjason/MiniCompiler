package cfg;

public interface SSCPValue
{
    String getString();

    class Top implements SSCPValue
    {
        public Top()
        {

        }

        public String getString()
        {
            return "Top";
        }

        @Override
        public boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (!(o instanceof Top)) {
                return false;
            }

            return true;
        }
    }

    class Bottom implements SSCPValue
    {
        public Bottom()
        {

        }

        public String getString()
        {
            return "Bottom";
        }

        @Override
        public boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (!(o instanceof Bottom)) {
                return false;
            }

            return true;
        }
    }

    class Constant implements SSCPValue
    {
        private int constant;
        private boolean bool;
        private boolean isBool;
        private boolean isNull;

        public Constant(int c)
        {
            this.constant = c;
            isBool = false;
        }

        public Constant(boolean c)
        {
            this.bool = c;
            isBool = true;
        }

        public Constant(String s)
        {
            isNull = true;
        }

        public String getString()
        {
            if (isBool)
            {
                return Boolean.toString(this.bool);
            }
            else if (isNull)
            {
                return "null";
            }
            else
            {
                return Integer.toString(this.constant);
            }
        }

        public Object getConst()
        {
            if (isBool)
            {
                return bool;
            }
            if (isNull)
            {
                return null;
            }
            else
            {
                return constant;
            }
        }

        @Override
        public boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (!(o instanceof Top)) {
                return false;
            }

            return ((Constant)o).getConst().equals(this.getConst()); // TODO?
        }
    }
}
