# CAFF Parser

## Build: 
```make caff_parser``` vagy ```make```

## Futtatás: 
```caff_parser.exe <input_file> <output_file>```

## Dependencys:
* [nlohmann/json](https://github.com/nlohmann/json) - .json fájl írása
* [nothings/stb image writer](https://github.com/nothings/stb) - .jpg fájl írása


## Tesztelés:
Az itt szereplő tesztek a program funkcionális teszteléséhez szolgálnak. A pozitív tesztek a sikeres betöltést tesztelik a megadott mintafájlokon. A negatív tesztekben a tesztfájlok különböző szélsőséges eseteket tartalmaznak, az itt elvárt működés a program leállása output generálás nélkül.

Az egyes tesztek külön-külön a .sh scriptekkel futtathatók az őket tartalmazó könyvtárakból, vagy a ```run_all_tests.sh``` script-tel a test könyvtárból.

Negatív tesztesetek:
- a fájl nem header block-kal kezdődik
- a fájl \n karaktert tartalmaz a tag-jeiben
- a fájlból hiányzik egy credits vagy animation block
- a fájl több bájtot tartalmaz mint ahány block specifikálva van
- a fájl tag-jeit nem zárja \0 karakter
- a fájl nem 'CAFF' magic-et tartalmaz
- a fájl nem 'CIFF' magic-et tartalmaz
- a fájl véletlen szerű bájt átírásokat/törléseket tartalmaz
- a fájlban kisebb az animáció block mérete mint amit specifikált
- a fájlban kisebb a content mérete mint amit specifikált
- a fájlban kisebb a credits block mint amit specifikált
- a fájlban kisebb a header mint amit specifikált
- a fájlban ismeretlen block következik
- a program nem kap elég paramétert
- a program túl sok paramétert kap
- a program nem .caff paramétert kap
- a program nem .json paramétert kap
- a program nem létező input fájlt kap paraméterként

A komponens további teszteléséről a projekt wiki oldalai között írunk részletesen.

## Dokumentáció:
A program dokumentációja megtalálható a projekt "CPP Parser komponens" wiki oldalán.
