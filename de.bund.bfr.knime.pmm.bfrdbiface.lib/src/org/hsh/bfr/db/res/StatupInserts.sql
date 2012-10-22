---------------------------------------------
-- Beispieldaten für die erstellten Tabellen
---------------------------------------------

INSERT INTO "Modellkatalog" 
("ID", "Name", "Notation", "Level", "Klasse", "Typ", "Eingabedatum", "eingegeben_von", "Beschreibung", "Formel", "Software", "Kommentar")
VALUES
(1, 'Baranyi', 'baranyi', 1, 1, 'Baranyi', '2011-11-11', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'LOG10N ~ LOG10Nmax + log10((-1 + exp(mumax * lag) + exp(mumax * t))/(exp(mumax * t) - 1 + exp(mumax * lag) * 10^(LOG10Nmax - LOG10N0)))', 'R', 'Formel kommt aus dem R-package nlstools'),
(2, 'Baranyi without Nmax', 'baranyi_without_Nmax', 1, 1, 'Baranyi', '2011-11-11', 'Alexander Engelhardt', 'Dummybeschreibung', 'LOG10N ~ LOG10N0 + mumax * t/log(10) + log10(exp(-mumax * t) * (1 - exp(-mumax * lag)) + exp(-mumax * lag))', 'R', 'Formel kommt aus dem R-package nlstools'),
(3, 'Baranyi without lag', 'baranyi_without_lag', 1, 1, 'Baranyi', '2011-11-30', 'Alexander Engelhardt', 'Dummybeschreibung', 'LOG10N ~ (LOG10Nmax - log10(1 + (10^(LOG10Nmax - LOG10N0) - 1) * exp(-mumax * t)))', 'R', 'Formel kommt aus dem R-package nlstools'),
(4, 'Buchanan', 'buchanan', 1, 1, 'Buchanan', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 + (t >= lag) * (t <= (lag + (LOG10Nmax - LOG10N0) * log(10)/mumax)) * mumax * (t - lag)/log(10) + (t >= lag) * (t > (lag + (LOG10Nmax - LOG10N0) * log(10)/mumax)) * (LOG10Nmax - LOG10N0)', 'R', 'Formel kommt aus dem R-package nlstools'),
(5, 'Buchanan without Nmax', 'buchanan_without_Nmax', 1, 1, 'Buchanan', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ (t <= lag) * LOG10N0 + (t > lag) * (LOG10N0 + mumax/log(10) * (t - lag))', 'R', 'Formel kommt aus dem R-package nlstools'),
(6, 'Buchanan without lag', 'buchanan_without_lag', 1, 1, 'Buchanan', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 + (t <= ((LOG10Nmax - LOG10N0) * log(10)/mumax)) * mumax * t/log(10) + (t > ((LOG10Nmax - LOG10N0) * log(10)/mumax)) * (LOG10Nmax - LOG10N0)', 'R', 'Formel kommt aus dem R-package nlstools'),
(7, 'Reparameterized Gompertz', 'gompertzm', 1, 1, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 + (LOG10Nmax - LOG10N0) * exp(-exp(mumax * exp(1) * (lag - t)/((LOG10Nmax - LOG10N0) * log(10)) + 1))', 'R', 'Formel aus dem R-package nlstools'),
(8, 'Linear model', 'lm', 1, 1, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'y~a+b*x', 'R', 'Standardformel'),
-- Klasse: Survival heißt '3' als int
(9, 'Geeraerd', 'geeraerd', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10Nres + log10((10^(LOG10N0 - LOG10Nres) - 1) * exp(kmax * Sl)/(exp(kmax * t) + (exp(kmax * Sl) - 1)) + 1)', 'R', 'Formel aus dem R-package nlstools'),
(10, 'Geeraerd', 'geeraerd_without_Nres', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 - kmax * t/log(10) + log10(exp(kmax * Sl)/(1 + (exp(kmax * Sl) - 1) * exp(-kmax * t)))', 'R', 'Formel aus dem R-package nlstools'),
(11, 'Geeraerd without Sl', 'geeraerd_without_Sl', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ (LOG10Nres + log10(1 + (10^(LOG10N0 - LOG10Nres) - 1) * exp(-kmax * t)))', 'R', 'Formel aus dem R-package nlstools'),
(12, 'Mafart', 'mafart', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 - (t/delta)^p', 'R', 'Formel aus dem R-package nlstools'),
(13, 'Albert', 'albert', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10Nres + log10((10^(LOG10N0 - LOG10Nres) - 1) * 10^(-(t/delta)^p) + 1)', 'R', 'Formel aus dem R-package nlstools'),
(14, 'Trilinear', 'trilinear', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 - (t >= Sl) * (t <= (Sl + (LOG10N0 - LOG10Nres) * log(10)/kmax)) * kmax * (t - Sl)/log(10) + (t >= Sl) * (t > (Sl + (LOG10N0 - LOG10Nres) * log(10)/kmax)) * (LOG10Nres - LOG10N0)', 'R', 'Formel aus dem R-package nlstools'),
(15, 'Bilinear without Nres', 'bilinear_without_Nres', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ (t <= Sl) * LOG10N0 + (t > Sl) * (LOG10N0 - kmax/log(10) * (t - Sl))', 'R', 'Formel aus dem R-package nlstools'),
(16, 'Bilinear without Sl', 'bilinear_without_Sl', 1, 3, '', '2011-11-30', 'Alexander Engelhardt', 'Beschreibung', 'LOG10N ~ LOG10N0 - (t <= ((LOG10N0 - LOG10Nres) * log(10)/kmax)) * kmax * t/log(10) + (t > ((LOG10N0 - LOG10Nres) * log(10)/kmax)) * (LOG10Nres - LOG10N0)', 'R', 'Formel aus dem R-package nlstools'),

-- Sekundärmodelle ab hier. Erstmal alle poly_X_Y:

-- ("ID", "Name", "Notation", "Level", "Klasse", "Typ", "Eingabedatum", "eingegeben_von", "Beschreibung", "Formel", "Software", "Kommentar")
-- 8: T, 9: pH, 10:aw, 11:T/pH, 12:T/aw, 13:pH/aw, 14:T/ph/aw
(17, 'Polynomial of 1. Order (Temperatur)', 'poly_T_1', 
  2, 8, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T', 'R', 'Stat-Up model'),
(18, 'Polynomial of 2. Order (Temperatur)', 'poly_T_2', 
  2, 8, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*(T^2)', 'R', 'Stat-Up model'),
(19, 'Polynomial of 1. Order (pH)', 'poly_pH_1', 
  2, 9, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH', 'R', 'Stat-Up model'),
(20, 'Polynomial of 2. Order (pH)', 'poly_pH_2', 
  2, 9, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*(pH^2)', 'R', 'Stat-Up model'),
(21, 'Polynomial of 1. Order (aw)', 'poly_1_aw', 
  2, 10, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*aw', 'R', 'Stat-Up model'),
(22, 'Polynomial of 2. Order (aw)', 'poly_2_aw', 
  2, 10, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*aw+b2*(aw^2)', 'R', 'Stat-Up model'),
(23, 'Polynomial of 1. Order (T & pH)', 'poly_T_pH_1', 
  2, 11, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*pH', 'R', 'Stat-Up model'),
(24, 'Polynomial of 1. Order (T & pH) with interaction', 'poly_T_pH_1_inter', 
  2, 11, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*pH+b3*T:pH', 'R', 'Stat-Up model'),
(25, 'Polynomial of 1. Order (T & aw)', 'poly_T_aw_1', 
  2, 12, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*aw', 'R', 'Stat-Up model'),
(26, 'Polynomial of 1. Order (T & aw) with interaction', 'poly_T_aw_1_inter', 
  2, 12, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*aw+b3*T:aw', 'R', 'Stat-Up model'),
(27, 'Polynomial of 1. Order (pH & aw)', 'poly_pH_aw_1', 
  2, 13, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*aw', 'R', 'Stat-Up model'),
(28, 'Polynomial of 1. Order (pH & aw) with interaction', 'poly_pH_aw_1_inter', 
  2, 13, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*aw+b3*pH:aw', 'R', 'Stat-Up model'),
(29, 'Polynomial of 2. Order (T & pH)', 'poly_T_pH_2', 
  2, 11, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*pH+b3*T^2+b4*(pH^2)', 'R', 'Stat-Up model'),
(30, 'Polynomial of 2. Order (T & aw)', 'poly_T_aw_2', 
  2, 12, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*aw+b3*T^2+b4*(aw^2)', 'R', 'Stat-Up model'),
(31, 'Polynomial of 2. Order (pH & aw)', 'poly_pH_aw_2', 
  2, 13, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*aw+b3*(pH^2)+b4*(aw^2)', 'R', 'Stat-Up model'),
(32, 'Polynomial of 2. Order (T & pH) without T^2', 'poly_T_pH_2_withoutT2', 
  2, 11, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*pH++b3*(pH^2)', 'R', 'Stat-Up model'),
(33, 'Polynomial of 2. Order (T & pH) without pH^2', 'poly_T_pH_2_witoutpH2', 
  2, 11, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*pH+b3*(T^2)', 'R', 'Stat-Up model'),
(34, 'Polynomial of 2. Order (T & aw) without T^2', 'poly_T_aw_2_withoutT2', 
  2, 12, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*aw+b3*(aw^2)', 'R', 'Stat-Up model'),
(35, 'Polynomial of 2. Order (T & aw) without aw^2', 'poly_T_aw_2_withoutaw2', 
  2, 12, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*T+b2*aw+b3*(T^2)', 'R', 'Stat-Up model'),
(36, 'Polynomial of 2. Order (pH & aw) without pH^2', 'poly_pH_aw_2_withoutpH2', 
  2, 13, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*aw+b3*(aw^2)', 'R', 'Stat-Up model'),
(37, 'Polynomial of 2. Order (pH & aw) without aw^2', 'poly_pH_aw_2_withoutaw2', 
  2, 13, '', '2011-12-05', 'Alexander Engelhardt', 'Beschreibung', 'y~b0+b1*pH+b2*aw+b3*(pH^2)', 'R', 'Stat-Up model');

-- nicht-polynomiale Sekundärmodelle:

INSERT INTO "Modellkatalog" 
("ID", "Name", "Notation", "Level", "Klasse", "Typ", "Eingabedatum", "eingegeben_von", "Beschreibung", "Formel", "Software", "Kommentar")
-- 8: T, 9: pH, 10:aw, 11:T/pH, 12:T/aw, 13:pH/aw, 14:T/ph/aw
VALUES
(38, 'cardinal temperature model (cpm_T)', 'cpm_T', 2, 8, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt(((T >= Tmin) & (T <= Tmax)) * muopt * (T - Tmax) * (T - Tmin)^2/((Topt - Tmin) * ((Topt - Tmin) * (T - Topt) - (Topt - Tmax) * (Topt + Tmin - 2 * T))))', 'R', 'Formel kommt aus dem R-package nlstools'),
(39, 'cardinal pH model (cpm_pH_4p)', 'cpm_pH_4p', 2, 9, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt(((pH >= pHmin) & (pH <= pHmax)) * muopt * (pH - pHmin) * (pH - pHmax)/((pH - pHmin) * (pH - pHmax) - (pH - pHopt)^2))', 'R', 'Formel kommt aus dem R-package nlstools'),
(40, 'symmetric cardinal pH model', 'cpm_pH_3p', 2, 9, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt(((pH >= pHmin) & (pH <= (2 * pHopt - pHmin))) * muopt * (pH - pHmin) * (pH - (2 * pHopt - pHmin))/((pH - pHmin) * (pH - (2 * pHopt - pHmin)) - (pH - pHopt)^2))', 'R', 'Formel kommt aus dem R-package nlstools'),
(41, 'cardinal aw model (cpm_aw_3p)', 'cpm_aw_3p', 2, 10, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt((aw >= awmin) * muopt * (aw - 1) * (aw - awmin)^2/((awopt - awmin) * ((awopt - awmin) * (aw - awopt) - (awopt - 1) * (awopt + awmin - 2 * aw))))', 'R', 'Formel kommt aus dem R-Package nlstools'),
(42, 'Simplified cardinal aw model', 'cpm_aw_2p', 2, 10, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt((aw >= awmin) * muopt * (aw - awmin)^2/(1 -awmin)^2)', 'R', 'Formel kommt aus dem R-Package nlstools'),
(43, 'cardinal model based on the gamma concept (cpm_T_pH_aw)', 'cpm_T_pH_aw', 2, 14, '', '2011-12-12', 'Alexander Engelhardt', 'Kurze Dummy-Modellbeschreibung', 'sqrtmumax ~ sqrt(((T >= Tmin) & (T <= Tmax) & (pH >= pHmin) & (pH <= (pHmax)) & (aw >= awmin)) * muopt * (T - Tmax) * (T - Tmin)^2/((Topt - Tmin) * ((Topt - Tmin) * (T - Topt) - (Topt - Tmax) * (Topt + Tmin - 2 * T))) * (pH - pHmin) * (pH - pHmax)/((pH - pHmin) * (pH - pHmax) - (pH - pHopt)^2) * (aw - 1) * (aw - awmin)^2/((awopt - awmin) * ((awopt - awmin) * (aw - awopt) - (awopt - 1) * (awopt + awmin - 2 * aw))))', 'R', 'Formel kommt aus dem R-package nlstools');


INSERT INTO "ModellkatalogParameter"
("ID", "Modell", "Parametername", "Parametertyp", "ganzzahl", "min", "max", "Beschreibung")
VALUES
-- baranyi
(1, 1, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(2, 1, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(3, 1, 'lag', 2, FALSE, NULL, NULL, 'Descripcion'),
(4, 1, 'LOG10Nmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(5, 1, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(6, 1, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 1'),
-- und fürs zweite Modell, baranyi_without_Nmax:
(7, 2, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(8, 2, 'mumax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(9, 2, 'lag', 2, FALSE, NULL, NULL, 'Beschreibung'),
(10, 2, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(11, 2, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 2'),
-- without_lag:
(12, 3, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(13, 3, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(14, 3, 'LOG10Nmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(15, 3, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(16, 3, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 3'),
-- Buchanan:
(17, 4, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(18, 4, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(19, 4, 'lag', 2, FALSE, NULL, NULL, 'Descripcion'),
(20, 4, 'LOG10Nmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(21, 4, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(22, 4, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Buchanan without Nmax:
(23, 5, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(24, 5, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(25, 5, 'lag', 2, FALSE, NULL, NULL, 'Descripcion'),
(26, 5, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(27, 5, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 5'),
-- Buchanan without lag:
(28, 6, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(29, 6, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(30, 6, 'LOG10Nmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(31, 6, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(32, 6, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 6'),
-- Reparameterized Gompertz:
(33, 7, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(34, 7, 'mumax', 2, FALSE, NULL, NULL, 'Description'),
(35, 7, 'lag', 2, FALSE, NULL, NULL, 'Descripcion'),
(36, 7, 'LOG10Nmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(37, 7, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(38, 7, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- lm
(39, 8, 'x', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(40, 8, 'y', 3, FALSE, NULL, NULL, 'Response Modell 4'),
(41, 8, 'a', 2, FALSE, NULL, NULL, 'Intercept'),
(42, 8, 'b', 2, FALSE, NULL, NULL, 'Slope'),
-- Geeraerd
(43, 9, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(44, 9, 'kmax', 2, FALSE, NULL, NULL, 'Description'),
(45, 9, 'Sl', 2, FALSE, NULL, NULL, 'Descripcion'),
(46, 9, 'LOG10Nres', 2, FALSE, NULL, NULL, 'Beschreibung'),
(47, 9, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(48, 9, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Geeraerd w/o Nres
(49, 10, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(50, 10, 'kmax', 2, FALSE, NULL, NULL, 'Description'),
(51, 10, 'Sl', 2, FALSE, NULL, NULL, 'Descripcion'),
(52, 10, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(53, 10, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- 11: Geeraerd w/o Sl
(54, 11, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(55, 11, 'kmax', 2, FALSE, NULL, NULL, 'Description'),
(56, 11, 'LOG10Nres', 2, FALSE, NULL, NULL, 'Beschreibung'),
(57, 11, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(58, 11, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Mafart
(59, 12, 'p', 2, FALSE, NULL, NULL, 'Description'),
(60, 12, 'delta', 2, FALSE, NULL, NULL, 'Beschreibung'),
(61, 12, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(62, 12, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(63, 12, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- albert
(64, 13, 'p', 2, FALSE, NULL, NULL, 'Description'),
(65, 13, 'LOG10Nres', 2, FALSE, NULL, NULL, 'Description'),
(66, 13, 'delta', 2, FALSE, NULL, NULL, 'Beschreibung'),
(67, 13, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(68, 13, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(69, 13, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Trilinear
(70, 14, 'Sl', 2, FALSE, NULL, NULL, 'Description'),
(71, 14, 'LOG10Nres', 2, FALSE, NULL, NULL, 'Description'),
(72, 14, 'kmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(73, 14, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(74, 14, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(75, 14, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Bilinear w/o Nres
(76, 15, 'Sl', 2, FALSE, NULL, NULL, 'Description'),
(77, 15, 'kmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(78, 15, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(79, 15, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(80, 15, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4'),
-- Bilinear w/o Sl
(81, 16, 'LOG10Nres', 2, FALSE, NULL, NULL, 'Description'),
(82, 16, 'kmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(83, 16, 'LOG10N0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(84, 16, 't', 1, FALSE, NULL, NULL, 'Zeitpunkt'),
(85, 16, 'LOG10N', 3, FALSE, NULL, NULL, 'Response Modell 4');

-- Sekundärmodellparameter:
INSERT INTO "ModellkatalogParameter"
("ID", "Modell", "Parametername", "Parametertyp", "ganzzahl", "min", "max", "Beschreibung")
VALUES
-- poly_T_1
(86, 17, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(87, 17, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(88, 17,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(89, 17,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_2
(90, 18, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(91, 18, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(92, 18, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(93, 18,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(94, 18,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_1
(95, 19, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(96, 19, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(97, 19, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(98, 19,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_2
(99, 20, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(100, 20, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(101, 20, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(102, 20, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(103, 20,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_1_aw
(104, 21, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(105, 21, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(106, 21, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(107, 21,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_2_aw
(108, 22, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(109, 22, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(110, 22, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(111, 22, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(112, 22,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_pH_1
(113, 23, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(114, 23, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(115, 23, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(116, 23,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(117, 23, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(118, 23,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_pH_1_inter
(119, 24, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(120, 24, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(121, 24, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(123, 24, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(124, 24,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(125, 24, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'), 
(126, 24,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_aw_1
(127, 25, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(128, 25, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(129, 25, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(130, 25,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(131, 25, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(132, 25,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_aw_1_inter
(133, 26, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(134, 26, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(135, 26, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(136, 26, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(137, 26,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(138, 26, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(139, 26,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_aw_1
(140, 27, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(141, 27, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(142, 27, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(143, 27, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(144, 27, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(145, 27,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_aw_1_inter
(146, 28, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(147, 28, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(148, 28, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(149, 28, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(150, 28, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(151, 28, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(152, 28,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_pH_2
(153, 29, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(154, 29, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(155, 29, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(156, 29, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(157, 29, 'b4', 2, FALSE, NULL, NULL, 'Beschreibung'),
(158, 29,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(159, 29, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(160, 29,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_aw_2
(161, 30, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(162, 30, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(163, 30, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(164, 30, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(165, 30, 'b4', 2, FALSE, NULL, NULL, 'Beschreibung'),
(166, 30,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(167, 30, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(168, 30,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_aw_2
(169, 31, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(170, 31, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(171, 31, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(172, 31, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(173, 31, 'b4', 2, FALSE, NULL, NULL, 'Beschreibung'),
(174, 31, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(175, 31, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(176, 31,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_pH_2_withoutT2
(177, 32, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(178, 32, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(179, 32, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(180, 32, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(181, 32,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(182, 32, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(183, 32,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_pH_2_withoutpH2
(184, 33, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(185, 33, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(186, 33, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(187, 33, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(188, 33,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(189, 33, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(190, 33,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_aw_2_withoutT2
(191, 34, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(192, 34, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(193, 34, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(194, 34, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(195, 34,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(196, 34, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(197, 34,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_T_aw_2_withoutaw2
(198, 35, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(199, 35, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(200, 35, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(201, 35, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(202, 35,  'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(203, 35, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(204, 35,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_aw_2_withoutpH2
(205, 36, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(206, 36, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(207, 36, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(208, 36, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(209, 36, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(210, 36, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(211, 36,  'y', 3, FALSE, NULL, NULL, 'Response'),
-- poly_pH_aw_2_withoutaw2
(212, 37, 'b0', 2, FALSE, NULL, NULL, 'Beschreibung'),
(213, 37, 'b1', 2, FALSE, NULL, NULL, 'Beschreibung'),
(214, 37, 'b2', 2, FALSE, NULL, NULL, 'Beschreibung'),
(215, 37, 'b3', 2, FALSE, NULL, NULL, 'Beschreibung'),
(216, 37, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(217, 37, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(218, 37,  'y', 3, FALSE, NULL, NULL, 'Response'),
-------------------
-- cpm_T
(219, 38, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(220, 38, 'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(221, 38, 'Tmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(222, 38, 'Topt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(223, 38, 'Tmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(224, 38, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
-- cpm_pH_4p
(225, 39, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(226, 39, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(227, 39, 'pHmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(228, 39, 'pHopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(229, 39, 'pHmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(230, 39, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
-- cpm_pH_3p
(231, 40, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(232, 40, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(233, 40, 'pHmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(234, 40, 'pHopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(235, 40, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
-- cpm_aw_3p
(236, 41, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(237, 41, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(238, 41, 'awmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(239, 41, 'awopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(240, 41, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
-- cpm_aw_2p
(241, 42, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(242, 42, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(243, 42, 'awmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(244, 42, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
-- cpm_T_pH_aw
(245, 43, 'sqrtmumax', 3, FALSE, NULL, NULL, 'Response'),
(246, 43, 'aw', 1, FALSE, NULL, NULL, 'Kovariable'),
(247, 43, 'pH', 1, FALSE, NULL, NULL, 'Kovariable'),
(248, 43, 'T', 1, FALSE, NULL, NULL, 'Kovariable'),
(249, 43, 'Tmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(250, 43, 'Topt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(251, 43, 'Tmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(252, 43, 'pHmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(253, 43, 'pHopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(254, 43, 'pHmax', 2, FALSE, NULL, NULL, 'Beschreibung'),
(255, 43, 'awmin', 2, FALSE, NULL, NULL, 'Beschreibung'),
(256, 43, 'awopt', 2, FALSE, NULL, NULL, 'Beschreibung'),
(257, 43, 'muopt', 2, FALSE, NULL, NULL, 'Beschreibung');
