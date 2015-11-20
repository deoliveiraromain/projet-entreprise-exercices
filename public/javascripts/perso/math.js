/**
 * Maxime Gautré, Romain De Olivera, Bastien Chares
 *
 * Les éléments mathématiques sont placés dans un système d'accordéons. Nous les chargeons dynamiquemeent, c'est à dire qu'ils seront chargés
 * lorsque l'accordéon sera ouvert. Nous appliquons le chargement au premier clic de l'utilisateur.
 *
 */
(function () {

    // Premier clic sur le panel "fonction mathématique", on charge les élements mathématiques dans un tableau
    // La seconde ligne permet de signifier à la librairie Mathjax de convertir les élements.
    $("#idFonctionMathematique").one('click', function () {
        $("#tablesMath").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Arccos">$ \\arccos $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Arcsin">$ \\arcsin $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Arctan">$ \\arctan $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Cosinus">$ \\cos $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Cosinus hyperbolique">$ \\cosh $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Cotangente">$ \\cot $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Degré">$ \\deg $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Déterminant">$ \\det $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Dimension">$ \\dim $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Exponentielle">$ \\exp $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Exponentielle">$ \\mathrm{e} $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="PGCD">$ \\gcd $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Homothétie">$ \\hom $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Noyau">$ \\ker $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Limite inférieure">$ \\liminf $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Limite supérieure">$ \\limsup $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Limite">$ \\lim $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Logarithme népérien">$ \\ln $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Logarithme">$ \\log $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Maximum">$ \\max $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Minimum">$ \\min $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Inférieur">$ \\inf $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Supérieur">$ \\sup $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sinus">$ \\sin $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sinus hyperbolique">$ \\sinh $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Tangente">$ \\tan $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Tangente hyperbolique">$ \\tanh $</td><td></td><td></td><td></td></tr></table>');
        $('.exo-latex').tooltip();
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tablesMath"]);
    });

    // Premier clic sur le panel "Fraction"
    $("#idFraction").one('click', function () {
        $("#tableFraction").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\dfrac{x}{y} $ </td><td class="exo-latex">$ \\dfrac{\\mathrm{d}x}{\\mathrm{d}y} $</td><td class="exo-latex">$ \\dfrac{\\partial x}{\\partial y} $</td><td class="exo-latex">$ \\dfrac{\\Delta x}{\\Delta y} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableFraction"]);
    });

    // Premier clic sur le panel "Intégrale"
    $("#idIntegrale").one('click', function () {
        $("#tableIntegrale").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\int_{0}^{\\infty} x \\mathrm{d}x $</td><td class="exo-latex">$ \\iint_{0}^{\\infty} x \\mathrm{d}x $</td><td class="exo-latex">$ \\iiint_{0}^{\\infty} x \\mathrm{d}x $</td><td class="exo-latex">$ \\oint_{0}^{1} \\theta \\mathrm{d}\theta $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableIntegrale"]);
    });

    // Premier clic sur le panel "Indice et Exposant"
    $("#idIndiceExposant").one('click', function () {
        $("#tableIndiceExposant").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ a_{b} $</td><td class="exo-latex">$ a^{b} $</td><td class="exo-latex">$ a_{c}^{b} $</td><td class="exo-latex">$ a_{i,j} $</td><td class="exo-latex">$ x_{y^{2}} $</td><td class="exo-latex">$ \\mathrm{e}^{2} $</td><td class="exo-latex">$ \\mathrm{e}^{i\theta} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableIndiceExposant"]);
    });

    // Premier clic sur le panel "Radical"
    $("#idRadical").one('click', function () {
        $("#tableRadical").append('<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\sqrt{x} $</td><td class="exo-latex">$ \\sqrt[3]{x} $</td><td class="exo-latex">$ \\sqrt[n]{x} $</td><td class="exo-latex">$ \\sqrt{a^2 + b^2} $</td><td class="exo-latex">$ \\dfrac{-b \\pm \\sqrt{b^2 -4ac}}{2a} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableRadical"]);
    });

    // Premier clic sur le panel "Grand Opérateur"
    $("#idGrandOperateur").one('click', function () {
        $("#tableGrandOperateur").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\sum_{i=0}^{n} x_{i} $</td><td class="exo-latex">$ \\prod_{k=1}^{n} A_{k} $</td><td class="exo-latex">$ \\coprod_{i=0}^{n} x_{i} $</td><td class="exo-latex">$ \\bigcap_{i}^{n} x_{i} $</td><td class="exo-latex">$ \\sum_{0 \\le i \\le m \\atop 0 < j < n } P(i,j) $</td></tr><tr><td class="exo-latex">$ \\bigcup_{i}^{n} x_{i} $</td><td class="exo-latex">$ \\bigvee_{i}^{n} x_{i} $</td><td class="exo-latex">$ \\bigwedge_{i}^{n} x_{i} $</td><td class="exo-latex">$ \\sum_{k} \\binom{n}{k} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableGrandOperateur"]);
    });

    // Premier clic sur le panel "Limite et Logarithme"
    $("#idLimLogarithme").one('click', function () {
        $("#tableLimLogarithme").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\ln(x) $</td><td class="exo-latex">$ \\log(x) $</td><td class="exo-latex">$ \\log_{2}(x) $</td><td class="exo-latex">$ \\sup \\{1,2,3\\} = 3 $</td></tr><tr><td class="exo-latex">$ \\min_{i<0} x_i $</td><td class="exo-latex">$ \\max_{i>0} x_i $</td><td class="exo-latex">$ \\lim_{n \\to \\infty} x_n $</td><td class="exo-latex">$ \\lim_{n \\to \\infty} ( 1 + \\frac{1}{n})^{n} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableLimLogarithme"]);
    });

    // Premier clic sur le panel "Dérivé, norme, vecteur, angle et binôme"
    $("#iddnvb").one('click', function () {
        $("#tablednvb").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\nabla $</td><td class="exo-latex">$ \\partial x $</td><td class="exo-latex">$ \\mathrm{d}x $</td><td class="exo-latex">$ \\dot x $</td><td class="exo-latex">$ \\ddot x $</td><td class="exo-latex">$ | x | $</td><td class="exo-latex">$ \\| x \\| $</td></tr><tr><td class="exo-latex">$ \\| x \\|_{2} $</td><td class="exo-latex">$ \\vec U $</td><td class="exo-latex">$ \\overrightarrow{AB} $</td><td class="exo-latex">$ \\widehat {POQ} $</td><td class="exo-latex">$ \\binom{n}{k} $</td><td></td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tablednvb"]);
    });

    // Premier clic sur le panel "Opérateur"
    $("#idOperateur").one('click', function () {
        $("#tableOperateur").append('' + '<h5>Opérateurs de bases</h5><table class="table table-bordered table-responsive "><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Plus">$ + $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Moins">$ - $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Mutliplication">$ \\times $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Division">$ \\div $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Plus ou moins">$ \\pm $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Moins ou plus">$ \\mp $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Proportionnel à">$ \\propto $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Barre oblique de division">$ / $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Astérix">$ \\ast $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Fonction composée">$ \\circ $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Intersection">$ \\cap $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Union">$ \\cup $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Union de multiensemble">$ \\uplus $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Chapeau carré">$ \\sqcap $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Coupe carré">$ \\sqcup $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Et logique">$ \\wedge $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Oui logique">$ \\vee $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Pour tous">$ \\forall $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Infini">$ \\infty $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Existe">$ \\exists $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="N\'existe pas">$ \\nexists $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Ensemble vide">$ \\emptyset $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Pourcentage">$ \\% $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Degré">$ ^{\\circ} $</td></tr></table><h5>Opérateurs relationnels courants</h5><table class="table table-bordered table-responsive"><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Egal">$ = $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Différent">$ \\neq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Inférieur">$ < $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Supérieur">$ > $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Inférieur ou égal">$ \\leq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Supérieur ou égal">$ \\geq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Pas plus petit que">$ \\nless $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Ni plus petit ni égal à">$ \\nleq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Pas plus grand que">$ \\ngtr $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Ni plus grand que ni égal à">$ \\ngeq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Identique à">$ \\equiv $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Similaire à">$ \\sim $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Asymptotiquement égal à">$ \\simeq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Approximativement égal à">$ \\approx $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Congru à">$ \\cong $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Pas congru à">$ \\ncong $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Beaucoup plus petit que">$ \\ll $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Beaucoup plus grand que">$ \\gg $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Appartient à">$ \\in $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="N\'appartient pas à">$ \\ni $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="N\'est pas similaire à">$ \\nsim $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="N\'est pas approximativement égal à">$ \\not\\approx $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sous ensemble de">$ \\subset $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sur ensemble de">$ \\supset $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sous ensemble de ou égal à">$ \\subseteq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Sur ensemble de ou égal à">$ \\supseteq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Précède">$ \\prec $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Suit">$ \\succ $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Précède ou est égal à">$ \\preceq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Suit ou est égal à">$ \\succeq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Image carrée de">$ \\sqsubset $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Original carré de">$ \\sqsupset $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Image carrée ou égal à">$ \\sqsubseteq $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Original carré ou égal à">$ \\sqsupseteq $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Séparateur">$ \\mid $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Parallèle à">$ \\parallel $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Perpendiculaire à">$ \\perp $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Vrai">$ \\models $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Prouve (produit)">$ \\vdash $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Ne produit pas">$ \\dashv $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Borne supérieure">$ \\bowtie $</td><td></td><td></td><td></td></tr></table><h5>Autres opérateurs courants</h5><table class="table table-bordered table-responsive"><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Somme">$ \\sum $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Produit">$ \\prod $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Coproduit">$ \\coprod $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Intégrale">$ \\int $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Double intégrale">$ \\iint $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Triple intégrale">$ \\iiint $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Intégrale de contour">$ \\oint $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Intersection n-aire">$ \\bigcap $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Union n-aire">$ \\bigcup $</td></tr><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur point cercle">$ \\odot $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur de multiplication">$ \\otimes $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur d\'addition">$ \\oplus $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur point cercle n-aire ">$ \\bigodot $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur de multiplication n-aire">$ \\bigotimes $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Opérateur d\'addition n-aire">$ \\bigoplus $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Racine carré">$ \\sqrt{} $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Ou logique n-aire">$ \\bigwedge $</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Et logique n-aire">$ \\bigvee $</td></tr></table>');
        $('.exo-latex').tooltip();
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableOperateur"]);
    });

    // Premier clic sur le panel "Délimiteurs"
    $("#idDelimitor").one('click', function () {
        $("#tableDelimitor").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ ( ) $</td><td class="exo-latex">$ [ ]  $</td><td class="exo-latex">$ [\\![  ]\\!] $</td><td class="exo-latex">$ \\{ \\} $</td><td class="exo-latex">$ \\lfloor  $</td><td class="exo-latex">$ \\rfloor $</td><td class="exo-latex">$ \\lceil  $</td><td class="exo-latex">$ \\rceil  $</td><td class="exo-latex">$ \\langle  $</td><td class="exo-latex">$ \\rangle  $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableDelimitor"]);
    });

    // Premier clic sur le panel "Flèches"
    $("#idArrow").one('click', function () {
        $("#tableArrow").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\leftarrow $</td><td class="exo-latex">$ \\longleftarrow $</td><td class="exo-latex">$ \\rightarrow $</td><td class="exo-latex">$ \\longrightarrow $</td><td class="exo-latex">$ \\leftrightarrow $</td><td class="exo-latex">$ \\longleftrightarrow $</td><td class="exo-latex">$ \\uparrow $</td><td class="exo-latex">$ \\downarrow $</td><td class="exo-latex">$ \\circlearrowleft $</td></tr><tr><td class="exo-latex">$ \\Leftarrow $</td><td class="exo-latex">$ \\Longleftarrow $</td><td class="exo-latex">$ \\Rightarrow $</td><td class="exo-latex">$ \\Longrightarrow $</td><td class="exo-latex">$ \\Leftrightarrow $</td><td class="exo-latex">$ \\Longleftrightarrow $</td><td class="exo-latex">$ \\Uparrow $</td><td class="exo-latex">$ \\Downarrow $</td><td class="exo-latex">$ \\circlearrowright $</td></tr><tr><td class="exo-latex">$ \\nleftarrow $</td><td class="exo-latex">$ \\nLeftarrow $</td><td class="exo-latex">$ \\nrightarrow $</td><td class="exo-latex">$ \\nRightarrow $</td><td class="exo-latex">$ \\nleftrightarrow $</td><td class="exo-latex">$ \\nLeftrightarrow $</td><td class="exo-latex">$ \\updownarrow $</td><td class="exo-latex">$ \\Updownarrow $</td><td class="exo-latex">$ \\multimap $</td></tr><tr><td class="exo-latex">$ \\hookleftarrow $</td><td class="exo-latex">$ \\leftleftarrows $</td><td class="exo-latex">$ \\hookrightarrow $</td><td class="exo-latex">$ \\rightrightarrows $</td><td class="exo-latex">$ \\leftrightarrows $</td><td class="exo-latex">$ \\rightleftarrows $</td><td class="exo-latex">$ \\upuparrows $</td><td class="exo-latex">$ \\downdownarrows $</td><td class="exo-latex">$ \\nearrow $</td></tr><tr><td class="exo-latex">$ \\leftharpoonup $</td><td class="exo-latex">$ \\leftleftarrows $</td><td class="exo-latex">$ \\rightharpoonup $</td><td class="exo-latex">$ \\rightrightarrows $</td><td class="exo-latex">$ \\rightleftharpoons $</td><td class="exo-latex">$ \\leftrightharpoons $</td><td class="exo-latex">$ \\upharpoonright $</td><td class="exo-latex">$ \\upharpoonleft $</td><td class="exo-latex">$ \\searrow $</td></tr><tr><td class="exo-latex">$ \\leftharpoondown $</td><td class="exo-latex">$ \\dashleftarrow $</td><td class="exo-latex">$ \\rightharpoondown $</td><td class="exo-latex">$ \\dashrightarrow $</td><td class="exo-latex">$ \\leftrightsquigarrow $</td><td class="exo-latex">$ \\Lleftarrow $</td><td class="exo-latex">$ \\downharpoonright $</td><td class="exo-latex">$ \\downharpoonleft $</td><td class="exo-latex">$ \\swarrow $</td></tr><tr><td class="exo-latex">$ \\leftarrowtail $</td><td class="exo-latex">$ \\twoheadleftarrow $</td><td class="exo-latex">$ \\rightarrowtail $</td><td class="exo-latex">$ \\twoheadrightarrow $</td><td class="exo-latex">$ \\curvearrowleft $</td><td class="exo-latex">$ \\curvearrowright $</td><td class="exo-latex">$ \\Lsh $</td><td class="exo-latex">$ \\Rsh $</td><td class="exo-latex">$ \\nwarrow $</td></tr><tr><td class="exo-latex">$ \\mapsto $</td><td class="exo-latex">$ \\longmapsto $</td><td class="exo-latex">$ \\looparrowleft  $</td><td class="exo-latex">$ \\looparrowright $</td><td></td><td></td><td></td><td></td><td></td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableArrow"]);
    });

    // Premier clic sur le panel "Alphabet Grec"
    $("#idGreek").one('click', function () {
        $("#tableGreek").append('' + '<h5>Lettres minuscules</h5><table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\alpha $</td><td class="exo-latex">$ \\beta $</td><td class="exo-latex">$ \\gamma $</td><td class="exo-latex">$ \\delta $</td><td class="exo-latex">$ \\epsilon $</td><td class="exo-latex">$ \\varepsilon $</td><td class="exo-latex">$ \\zeta $</td><td class="exo-latex">$ \\eta $</td><td class="exo-latex">$ \\theta $</td><td class="exo-latex">$ \\vartheta $</td></tr><tr><td class="exo-latex">$ \\iota $</td><td class="exo-latex">$ \\kappa $</td><td class="exo-latex">$ \\lambda $</td><td class="exo-latex">$ \\mu $</td><td class="exo-latex">$ \\nu $</td><td class="exo-latex">$ \\xi $</td><td class="exo-latex">$ \\omicron $</td><td class="exo-latex">$ \\pi $</td><td class="exo-latex">$ \\varpi $</td><td class="exo-latex">$ \\rho $</td></tr><tr><td class="exo-latex">$ \\varrho $</td><td class="exo-latex">$ \\sigma $</td><td class="exo-latex">$ \\varsigma $</td><td class="exo-latex">$ \\tau $</td><td class="exo-latex">$ \\upsilon $</td><td class="exo-latex">$ \\phi $</td><td class="exo-latex">$ \\varphi $</td><td class="exo-latex">$ \\chi $</td><td class="exo-latex">$ \\psi $</td><td class="exo-latex">$ \\omega $</td></tr></table><h5>Lettres majuscules</h5><table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\Gamma $</td><td class="exo-latex">$ \\Delta $</td><td class="exo-latex">$ \\Theta $</td><td class="exo-latex">$ \\Lambda $</td><td class="exo-latex">$ \\Xi $</td><td class="exo-latex">$ \\Pi $</td><td class="exo-latex">$ \\Sigma $</td><td class="exo-latex">$ \\Upsilon $</td><td class="exo-latex">$ \\Phi $</td><td class="exo-latex">$ \\Psi $</td><td class="exo-latex">$ \\Omega $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableGreek"]);
    });

    // Premier clic sur le panel "Divers"
    $("#idDivers").one('click', function () {
        $("#tableDivers").append('' + '<table class="table table-bordered table-responsive"><tr><td class="exo-latex">$ \\imath $</td><td class="exo-latex">$ \\jmath $</td><td class="exo-latex">$ \\natural $</td><td class="exo-latex">$ \\flat $</td><td class="exo-latex">$ \\sharp $</td><td class="exo-latex">$ \\clubsuit $</td><td class="exo-latex">$ \\spadesuit $</td><td class="exo-latex">$ \\heartsuit $</td><td class="exo-latex">$ \\diamondsuit $</td><td class="exo-latex">$ \\lozenge $</td><td class="exo-latex">$ \\square $</td></tr><tr><td class="exo-latex">$ \\triangle $</td><td class="exo-latex">$ \\Re $</td><td class="exo-latex">$ \\Im $</td><td class="exo-latex">$ \\varnothing $</td><td class="exo-latex">$ \\complement $</td><td class="exo-latex">$ \\angle $</td><td class="exo-latex">$ \\measuredangle $</td><td class="exo-latex">$ \\mho $</td><td class="exo-latex">$ \\sphericalangle $</td><td class="exo-latex">$ \\blacksquare $</td><td class="exo-latex">$ \\blacklozenge $</td></tr><tr><td class="exo-latex">$ \\bigstar $</td><td class="exo-latex">$ \\diagup $</td><td class="exo-latex">$ \\diagdown $</td><td class="exo-latex">$ \\cdots $</td><td class="exo-latex">$ \\text{txt} $</td><td class="exo-latex">$ \\mathbb{N} $</td><td class="exo-latex">$ \\mathbb{Z} $</td><td class="exo-latex">$ \\mathbb{D} $</td><td class="exo-latex">$ \\mathbb{Q} $</td><td class="exo-latex">$ \\mathbb{R} $</td><td class="exo-latex">$ \\mathbb{C} $</td></tr></table>');
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableDivers"]);
    });

    // Premier clic sur le panel "Matrice"
    $("#idMatrix").one('click', function () {
        $("#tableMatrix").append('' + '<table class="table"><tr><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Matrice avec pointillés">' + '$ \\begin{matrix} a & \\cdots & b \\\\ \\vdots & \\ddots & \\vdots \\\\ c & \\cdots & d \\end{matrix} $' + '</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Matrice 2x2">' + '$ \\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} $' + '</td><td class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Matrice identité 2x2">' + '$ \\begin{pmatrix} 1 & 0 \\\\ 0 & 1 \\end{pmatrix} $' + '</td></tr></table>');
        $('.exo-latex').tooltip();
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableMatrix"]);
    });

    // Premier clic sur le panel "Equation"
    $("#idEquation").one('click', function () {
        $("#tableEquation").append('' + '<div id="eqLorentz" class="exo-latex " data-container="body" data-toggle="tooltip" data-placement="top" title="Equations de Lorentz">' + '$ \\begin{aligned} \\dot{x} & = \\sigma(y-x) \\\\ \\dot{y} & = \\rho x - y - xz \\\\ \\dot{z} & = -\\beta z + xy \\end{aligned} $' + '</div><br/><div id="systEq" class="exo-latex" data-container="body" data-toggle="tooltip" data-placement="top" title="Systèmes d\'équations">' + '$ f(n)=\\begin{cases} \\frac n2, & \\text{si }n\\text{ est pair} \\\\ 3n+1, & \\text{si }n\\text{ est impair} \\end{cases} $' + '</div>');
        $('.exo-latex').tooltip();
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "tableEquation"]);
    });

    // Fonction qui met à jour le latex sous la textarea dans le panel mathématique.
    function updateTex(Tex) {
        $("#MathOutput").html(Tex);
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "MathOutput"]);
    }

    // A chaque fois que l'utilisateur tape quelque chose dans la textarea, on récupère le contenu et on met à jour le latex sous la textarea
    $('#MathInput').keyup(function () {
        var keyed = $(this).val();
        updateTex('$ ' + keyed + ' $ ');
    });

    // Selon le panel où se trouve l'élément sélectionné, on va récupérer son contenu et extraire la formule latex.
    // On récupère l'élement entier puis on coupe la chaîne par les espaces. On enlève les éléments vides, puis on récupère uniquement
    // la formule (indexSlice). On met à jour la formule sous la textarea.
    function parseLatexInput(currentEl, indexSlice) {
        var text = $(currentEl).text().split(" ");
        var newValue = text.filter(function (el) {
            return el !== ""
        }).slice(indexSlice).join(" ");
        $("#MathInput").val($("#MathInput").val() + newValue + ' ');
        updateTex('$ ' + $("#MathInput").val() + ' $ ');
    }

    // Si l'élément cliqué a la classe "exo-latex". Ce sont tous les éléments mathématiques sauf ceux du panel équation.
    $(document).on('click', 'td[class^="exo-latex"]', function () {
        parseLatexInput(this, 1);
    });

    // Si l'élément cliqué est l'équation de Lorentz.
    $(document).on('click', '#eqLorentz', function () {
        parseLatexInput(this, 1);
    });

    // Si l'élément cliqué est un système d'équations.
    $(document).on('click', '#systEq', function () {
        parseLatexInput(this, 3);
    });

    // Cette fonction permet de créer une matrice. On récupère le nombre de lignes, le nombre de colonnes et le type de bordures,
    // puis on génère le code Latex de la matrice. La libraire MathJax se charge de l'interpréter.
    $("#createMatrix").click(function () {
        var border = $("#selectBorder").val();
        var column = $("#selectColumn").val();
        var row = $("#selectRow").val();
        var matrix = '\\begin{' + border + 'matrix}\n';
        for (var i = 0; i < row; ++i) {
            for (var j = 0; j < column; ++j) {
                if (j == 0) {
                    if (i == j) {
                        matrix += "1"
                    } else {
                        matrix += "0"
                    }
                    continue;
                }
                if (i == j) {
                    matrix += "& 1"
                } else {
                    matrix += "& 0"
                }
            }
            if (i !== row - 1)matrix += "\\\\ \n"
        }
        matrix += '\n \\end{' + border + 'matrix}';
        $("#MathInput").val($("#MathInput").val() + matrix + ' ');
        updateTex('$ ' + $("#MathInput").val() + ' $ ')
    });

    // Permet de nettoyer la textarea
    $("#buttonClearTextarea").click(function () {
        $("#MathOutput").empty();
        $("#MathInput").val('');
    });

})();