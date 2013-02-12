DROP VIEW IF EXISTS "IndepVarView";


CREATE VIEW "IndepVarView" AS

    SELECT
	  "GeschaetzteModelle"."ID" AS "GeschaetztesModell",
        ARRAY_AGG(
            CASE
                WHEN "VarParMap" IS NOT NULL
                THEN "VarParMap"
                ELSE "Parametername"
            END
        )AS "Independent",
        ARRAY_AGG( "Gueltig_von" )AS "minIndep",
        ARRAY_AGG( "Gueltig_bis" )AS "maxIndep"

    FROM "Modellkatalog"

    LEFT JOIN "GeschaetzteModelle"
    ON "Modellkatalog"."ID"="GeschaetzteModelle"."Modell"

     LEFT JOIN "GueltigkeitsBereiche"
    ON "GeschaetzteModelle"."ID"="GueltigkeitsBereiche"."GeschaetztesModell"

    LEFT JOIN "ModellkatalogParameter"
    ON "ModellkatalogParameter"."Modell"="Modellkatalog"."ID"

    LEFT JOIN "VarParMaps"
    ON "VarParMaps"."VarPar"="ModellkatalogParameter"."ID" AND "VarParMaps"."GeschaetztesModell"="GeschaetzteModelle"."ID"

    WHERE "Parametertyp"=1 AND "GeschaetzteModelle"."ID" IS NOT NULL
   GROUP BY "GeschaetzteModelle"."ID";


GRANT SELECT ON TABLE "IndepVarView" TO "PUBLIC";				
