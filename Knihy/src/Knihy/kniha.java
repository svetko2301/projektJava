package Knihy;

class kniha {
    public kniha(String nazov, String autor, int rokVydania, String typ, String zaner, boolean dostupnost) {
        this.nazov = nazov;
        this.autor = autor;
        this.rokVydania = rokVydania;
        this.typ = typ;
        this.zaner = zaner;
        this.dostupnost = dostupnost;
    }

    public kniha(String title, String author, int year, boolean available, String genre) {
		// TODO Auto-generated constructor stub
	}


	public String getNazov() {
        return nazov;
    }

    public String getAutor() {
        return autor;
    }

    public int getRokVydania() {
        return rokVydania;
    }

    public String getTyp() {
        return typ;
    }

    public String getZanerRocnik() {
        return zaner;
    }

    public boolean getDostupne() {
        return dostupnost;
    }


    private String nazov;
    private String autor;
    private int rokVydania;
    private String typ;
    private String zaner;
    private boolean dostupnost;

}
