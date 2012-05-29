package computation.sideralis.data;

import computation.sideralis.LocalizationSupport;
import computation.sideralis.object.ConstellationObject;


/**
 * This class contains all data for constellations definitions
 * @author Bernard
 */
public class ConstellationCatalog {
    public static final short ABBR = 0;
    public static final short LATIN = 1;
    public static final short NAME = 2;
    public static final short HIST = 3;
    
    // The definition of constellations
    private static final ConstellationObject[] constellations = {
new ConstellationObject(new short[]{534, 547, 547, 572, 572, 558, 558, 533, 533, 547}, 4, (short)558),
new ConstellationObject(new short[]{0, 23, 23, 48}, 0, (short)23),
new ConstellationObject(new short[]{462, 463, 462, 457, 462, 474, 476, 477, 474, 478}, 5, (short)474),
new ConstellationObject(new short[]{408, 396, 396, 382, 382, 389}, 48, (short)382),
new ConstellationObject(new short[]{61, 63, 63, 55, 55, 52, 52, 56, 56, 61, 55, 44, 44, 12, 12, 4, 4, 26, 26, 36, 36, 51, 51, 55, 4, 22, 22, 34, 34, 12}, 20, (short)36),
new ConstellationObject(new short[]{49, 40, 40, 39}, 6, (short)40),
new ConstellationObject(new short[]{243, 248, 248, 253}, 68, (short)248),
new ConstellationObject(new short[]{363, 368, 368, 381, 381, 394, 394, 387, 387, 372, 372, 371, 371, 368}, 8, (short)387),
new ConstellationObject(9, (short)116),
new ConstellationObject(new short[]{237, 293, 293, 297}, 21, (short)237),
new ConstellationObject(new short[]{250, 244, 244, 246, 246, 234, 246, 255}, 11, (short)246),
new ConstellationObject(new short[]{574, 575, 575, 597, 597, 605, 605, 611, 611, 614, 614, 607, 607, 593, 593, 589, 589, 578, 578, 575}, 15, (short)596),
new ConstellationObject(new short[]{176, 260, 260, 274, 274, 294, 294, 285, 285, 260, 274, 262, 262, 236, 236, 227}, 16, (short)262),
new ConstellationObject(new short[]{1, 10, 10, 15, 15, 27, 27, 38}, 17, (short)15),
new ConstellationObject(new short[]{375, 358, 358, 364, 358, 362, 362, 339, 339, 358, 339, 320, 320, 313, 320, 304, 362, 360, 360, 366, 366, 370, 370, 360, 360, 354, 360, 373, 373, 386}, 18, (short)360),
new ConstellationObject(new short[]{604, 609, 609, 664, 664, 645, 645, 623, 604, 623}, 19, (short)645),
new ConstellationObject(24, (short)352),
new ConstellationObject(new short[]{346, 334}, 12, (short)346),
new ConstellationObject(new short[]{133, 163, 163, 164, 164, 139, 139, 124, 124, 127, 127, 133}, 7, (short)136),
new ConstellationObject(new short[]{174, 165, 165, 159, 159, 152, 152, 142}, 23, (short)152),
new ConstellationObject(new short[]{398, 376, 376, 392}, 22, (short)376),
new ConstellationObject(new short[]{331, 326, 326, 322, 322, 321, 322, 335, 335, 331}, 27, (short)326),
new ConstellationObject(new short[]{308, 300, 300, 305, 305, 310, 310, 308}, 28, (short)308),
new ConstellationObject(new short[]{528, 536, 536, 538, 538, 535, 535, 530}, 25, (short)536),
new ConstellationObject(new short[]{407, 404, 404, 410, 410, 412, 412, 418, 418, 425}, 26, (short)410),
new ConstellationObject(new short[]{330, 332, 323, 341}, 29, (short)341),
new ConstellationObject(new short[]{599, 591, 591, 577, 577, 587, 577, 565, 565, 550, 577, 555, 555, 551, 551, 543}, 30, (short)565),
new ConstellationObject(new short[]{590, 585, 585, 582, 590, 588, 588, 583, 583, 579}, 31, (short)583),
new ConstellationObject(new short[]{99, 112, 112, 149}, 32, (short)112),
new ConstellationObject(new short[]{495, 480, 480, 482, 482, 492, 492, 495, 492, 542, 542, 561, 561, 509, 509, 469, 469, 445, 445, 431, 431, 403, 403, 367, 367, 336, 336, 311}, 33, (short)561),
new ConstellationObject(new short[]{518, 511, 511, 510}, 74, (short)511),
new ConstellationObject(new short[]{32, 43, 43, 50, 50, 53, 53, 54, 54, 62, 62, 101, 101, 111, 111, 107, 107, 87, 87, 83, 83, 76, 76, 70, 70, 65, 65, 59, 59, 57, 57, 60, 60, 69, 69, 75, 75, 80, 80, 82, 82, 89, 89, 94, 94, 110, 110, 117, 117, 130}, 35, (short)0),
new ConstellationObject(new short[]{553, 556, 556, 554, 556, 568}, 70, (short)556),
new ConstellationObject(new short[]{67, 58}, 36, (short)67),
new ConstellationObject(new short[]{184, 208, 208, 210, 210, 198, 198, 180, 210, 218, 218, 222, 218, 223, 218, 212, 212, 203, 203, 217, 203, 189, 203, 183, 183, 177, 183, 172, 172, 169}, 37, (short)212),
new ConstellationObject(new short[]{118, 125}, 10, (short)125),
new ConstellationObject(new short[]{185, 181, 181, 173, 181, 179, 181, 194, 194, 199, 199, 185, 194, 196, 196, 171, 196, 188, 196, 197, 197, 202, 202, 207, 207, 214, 202, 199, 185, 195, 195, 201, 201, 193, 193, 195}, 13, (short)199),
new ConstellationObject(new short[]{359, 355, 355, 343, 343, 324, 324, 319, 319, 301, 301, 302, 302, 324}, 82, (short)324),
new ConstellationObject(new short[]{644, 640, 640, 630, 630, 619, 630, 615}, 38, (short)640),
new ConstellationObject(new short[]{490, 471, 471, 466, 466, 452, 452, 447, 447, 470, 447, 440, 452, 456, 456, 439, 456, 472, 472, 493, 493, 487, 472, 466}, 39, (short)452),
new ConstellationObject(40, (short)96),
new ConstellationObject(new short[]{365, 353, 353, 350, 350, 318, 318, 312, 312, 303, 303, 296, 296, 288, 288, 283, 283, 278, 278, 275, 275, 267, 267, 272, 272, 259, 259, 254, 254, 251, 251, 241, 241, 245, 245, 242, 242, 251}, 41, (short)267),
new ConstellationObject(new short[]{45, 6, 6, 86, 86, 45}, 42, (short)45),
new ConstellationObject(new short[]{580, 603, 603, 594, 603, 616}, 43, (short)580),
new ConstellationObject(new short[]{628, 636, 636, 629, 629, 632, 632, 627, 627, 635, 635, 624}, 44, (short)636),
new ConstellationObject(new short[]{231, 221, 221, 204, 204, 187, 187, 186, 187, 175, 204, 178, 178, 170}, 54, (short)204),
new ConstellationObject(new short[]{131, 129, 129, 140, 140, 143, 143, 131, 143, 155, 155, 162, 162, 167, 167, 158, 158, 154, 154, 140, 131, 135, 131, 132}, 47, (short)143),
new ConstellationObject(new short[]{282, 307, 307, 316, 316, 306, 306, 286, 286, 280, 280, 282, 280, 284, 284, 276, 276, 273}, 45, (short)286),
new ConstellationObject(new short[]{397, 406, 406, 400, 400, 390, 390, 374, 374, 378, 378, 385, 385, 397, 397, 399, 406, 426, 426, 432, 400, 395, 395, 391, 378, 369}, 49, (short)400),
new ConstellationObject(new short[]{264, 261, 261, 235}, 50, (short)264),
new ConstellationObject(new short[]{514, 516, 516, 520, 520, 529, 529, 525, 525, 516}, 51, (short)516),
new ConstellationObject(new short[]{291, 269, 291, 299}, 1, (short)291),
new ConstellationObject(53, (short)595),
new ConstellationObject(new short[]{315, 328, 328, 337, 337, 348, 337, 333}, 55, (short)337),
new ConstellationObject(57, (short)610),
new ConstellationObject(new short[]{379, 449, 449, 441, 441, 379}, 2, (short)379),
new ConstellationObject(new short[]{494, 491, 491, 488, 488, 483, 483, 464, 464, 448, 448, 433, 433, 437, 437, 444, 444, 451, 451, 467, 467, 473}, 58, (short)433),
new ConstellationObject(new short[]{156, 151, 151, 161, 161, 145, 145, 138, 138, 141, 141, 137, 137, 134, 151, 147, 147, 141, 161, 138, 161, 166, 166, 168, 138, 119, 123, 120, 120, 119, 119, 121, 121, 122, 151, 146, 146, 147}, 59, (short)150),
new ConstellationObject(new short[]{576, 570, 570, 586, 586, 606, 570, 563, 563, 513, 513, 521, 521, 570, 521, 519, 519, 501, 501, 496, 496, 486}, 60, (short)521),
new ConstellationObject(new short[]{613, 622, 622, 654, 654, 3, 3, 0, 0, 652, 652, 642, 652, 643, 643, 620, 652, 654}, 61, (short)652),
new ConstellationObject(new short[]{190, 160, 160, 157}, 64, (short)190),
new ConstellationObject(new short[]{64, 71, 71, 66, 71, 78, 78, 88, 88, 85}, 62, (short)78),
new ConstellationObject(new short[]{601, 600, 600, 598}, 34, (short)601),
new ConstellationObject(new short[]{219, 215}, 14, (short)219),
new ConstellationObject(new short[]{298, 289, 289, 279, 279, 271}, 46, (short)289),
new ConstellationObject(new short[]{541, 548, 548, 564, 564, 569, 569, 573}, 87, (short)548),
new ConstellationObject(new short[]{28, 498, 498, 465, 465, 421, 421, 443, 443, 402, 402, 384, 384, 421}, 83, (short)421),
new ConstellationObject(new short[]{7, 8, 8, 2, 8, 11, 11, 20, 20, 25, 25, 13, 25, 31, 31, 42, 31, 29, 29, 20}, 63, (short)20),
new ConstellationObject(new short[]{651, 639, 639, 612, 612, 621, 621, 633, 633, 647, 650, 651}, 66, (short)633),
new ConstellationObject(new short[]{256, 239, 239, 211, 211, 206, 206, 225, 225, 233, 233, 239}, 86, (short)239),
new ConstellationObject(new short[]{30, 35, 35, 47, 47, 33, 33, 19, 19, 14, 14, 667, 667, 663, 663, 665, 665, 660, 660, 658, 658, 661, 661, 663}, 65, (short)667),
new ConstellationObject(new short[]{228, 216, 216, 192, 192, 176, 176, 182, 182, 209, 209, 228, 209, 224, 224, 226, 226, 230, 230, 228}, 67, (short)228),
new ConstellationObject(new short[]{442, 435, 435, 428, 428, 429, 429, 442}, 56, (short)435),
new ConstellationObject(new short[]{97, 102, 102, 91, 91, 84, 84, 97}, 69, (short)97),
new ConstellationObject(new short[]{499, 508, 508, 502, 502, 504, 504, 500, 500, 497, 497, 499, 497, 502, 502, 515, 515, 523, 523, 539, 523, 531, 531, 546, 546, 544, 544, 560, 560, 567, 567, 532, 532, 523, 523, 527, 527, 539, 539, 545, 539, 523}, 71, (short)539),
new ConstellationObject(new short[]{424, 438, 438, 427, 438, 430, 438, 446, 446, 450, 450, 458, 458, 460, 460, 461, 461, 468, 468, 481, 481, 489, 489, 485, 485, 479}, 72, (short)458),
new ConstellationObject(new short[]{17, 666, 666, 659, 659, 662}, 73, (short)17),
new ConstellationObject(new short[]{416, 415, 415, 423, 415, 409, 409, 413, 413, 419, 419, 417, 417, 433, 526, 503, 503, 494, 494, 484, 484, 467}, 75, (short)413),
new ConstellationObject(76, (short)281),
new ConstellationObject(new short[]{72, 73, 73, 74, 73, 90, 90, 100, 100, 103, 103, 105, 105, 115, 115, 139, 115, 128, 128, 148, 148, 108, 108, 105, 108, 100, 108, 114, 114, 109, 109, 95, 95, 92, 92, 77, 77, 72, 115, 104, 104, 93, 93, 81, 93, 105}, 77, (short)105),
new ConstellationObject(new short[]{505, 507, 507, 524, 524, 571, 571, 552, 552, 505}, 78, (short)505),
new ConstellationObject(new short[]{625, 656, 656, 9, 656, 5}, 81, (short)656),
new ConstellationObject(new short[]{454, 420, 420, 405, 405, 393, 393, 454}, 80, (short)454),
new ConstellationObject(new short[]{617, 608, 608, 592, 617, 626, 626, 631, 638, 648, 648, 649, 649, 655}, 3, (short)617),
new ConstellationObject(new short[]{356, 351, 351, 357, 351, 340, 340, 344, 344, 349, 340, 329, 329, 317, 317, 314}, 85, (short)344),
new ConstellationObject(new short[]{257, 232, 232, 228, 232, 227, 227, 252, 252, 266, 266, 277, 277, 295, 295, 270, 270, 257}, 84, (short)257),
    };
    // The text associated with constellations
    public static final String[][] constNames = {
        {"And", "Andromeda", LocalizationSupport.getMessage("CONST_AND"), LocalizationSupport.getMessage("CONST_HIS_AND")},
        {"Ant", "Antlia", LocalizationSupport.getMessage("CONST_ANT"), LocalizationSupport.getMessage("CONST_HIS_ANT")},
        {"Aps", "Apus", LocalizationSupport.getMessage("CONST_APS"), LocalizationSupport.getMessage("CONST_HIS_APS")},
        {"Aqr", "Aquarius", LocalizationSupport.getMessage("CONST_AQR"), LocalizationSupport.getMessage("CONST_HIS_AQR")},
        {"Aql", "Aquila", LocalizationSupport.getMessage("CONST_AQL"), LocalizationSupport.getMessage("CONST_HIS_AQL")},
        {"Ara", "Ara", LocalizationSupport.getMessage("CONST_ARA"), LocalizationSupport.getMessage("CONST_HIS_ARA")},
        {"Ari", "Aries", LocalizationSupport.getMessage("CONST_ARI"), LocalizationSupport.getMessage("CONST_HIS_ARI")},
        {"Aur", "Auriga", LocalizationSupport.getMessage("CONST_AUR"), LocalizationSupport.getMessage("CONST_HIS_AUR")},
        {"Boo", "Bootes", LocalizationSupport.getMessage("CONST_BOO"), LocalizationSupport.getMessage("CONST_HIS_BOO")},
        {"Cae", "Caelum", LocalizationSupport.getMessage("CONST_CAE"), LocalizationSupport.getMessage("CONST_HIS_CAE")},
        {"Cam", "Camelopardalis", LocalizationSupport.getMessage("CONST_CAM"), LocalizationSupport.getMessage("CONST_HIS_CAM")},
        {"Cnc", "Cancer", LocalizationSupport.getMessage("CONST_CNC"), LocalizationSupport.getMessage("CONST_HIS_CNC")},
        {"CVn", "Canes Venatici", LocalizationSupport.getMessage("CONST_CVN"), LocalizationSupport.getMessage("CONST_HIS_CVN")},
        {"CMa", "Canis Major", LocalizationSupport.getMessage("CONST_CMA"), LocalizationSupport.getMessage("CONST_HIS_CMA")},
        {"CMi", "Canis Minor", LocalizationSupport.getMessage("CONST_CMI"), LocalizationSupport.getMessage("CONST_HIS_CMI")},
        {"Cap", "Capricornus", LocalizationSupport.getMessage("CONST_CAP"), LocalizationSupport.getMessage("CONST_HIS_CAP")},
        {"Car", "Carina", LocalizationSupport.getMessage("CONST_CAR"), LocalizationSupport.getMessage("CONST_HIS_CAR")},
        {"Cas", "Cassiopeia", LocalizationSupport.getMessage("CONST_CAS"), LocalizationSupport.getMessage("CONST_HIS_CAS")},
        {"Cen", "Centaurus", LocalizationSupport.getMessage("CONST_CEN"), LocalizationSupport.getMessage("CONST_HIS_CEN")},
        {"Cep", "Cepheus", LocalizationSupport.getMessage("CONST_CEP"), LocalizationSupport.getMessage("CONST_HIS_CEP")},
        {"Cet", "Cetus", LocalizationSupport.getMessage("CONST_CET"), LocalizationSupport.getMessage("CONST_HIS_CET")},
        {"Cha", "Chamaeleon", LocalizationSupport.getMessage("CONST_CHA"), LocalizationSupport.getMessage("CONST_HIS_CHA")},
        {"Cir", "Circinus", LocalizationSupport.getMessage("CONST_CIR"), LocalizationSupport.getMessage("CONST_HIS_CIR")},
        {"Col", "Columba", LocalizationSupport.getMessage("CONST_COL"), LocalizationSupport.getMessage("CONST_HIS_COL")},
        {"Com", "Coma Berenices", LocalizationSupport.getMessage("CONST_COM"), LocalizationSupport.getMessage("CONST_HIS_COM")},
        {"CrA", "Corona Austrina", LocalizationSupport.getMessage("CONST_CRA"), LocalizationSupport.getMessage("CONST_HIS_CRA")},
        {"CrB", "Corona Borealis", LocalizationSupport.getMessage("CONST_CRB"), LocalizationSupport.getMessage("CONST_HIS_CRB")},
        {"Crv", "Corvus", LocalizationSupport.getMessage("CONST_CRV"), LocalizationSupport.getMessage("CONST_HIS_CRV")},
        {"Crt", "Crater", LocalizationSupport.getMessage("CONST_CRT"), LocalizationSupport.getMessage("CONST_HIS_CRT")},
        {"Cru", "Crux", LocalizationSupport.getMessage("CONST_CRU"), LocalizationSupport.getMessage("CONST_HIS_CRU")},
        {"Cyg", "Cygnus", LocalizationSupport.getMessage("CONST_CYG"), LocalizationSupport.getMessage("CONST_HIS_CYG")},
        {"Del", "Delphinus", LocalizationSupport.getMessage("CONST_DEL"), LocalizationSupport.getMessage("CONST_HIS_DEL")},
        {"Dor", "Dorado", LocalizationSupport.getMessage("CONST_DOR"), LocalizationSupport.getMessage("CONST_HIS_DOR")},
        {"Dra", "Draco", LocalizationSupport.getMessage("CONST_DRA"), LocalizationSupport.getMessage("CONST_HIS_DRA")},
        {"Equ", "Equuleus", LocalizationSupport.getMessage("CONST_EQU"), LocalizationSupport.getMessage("CONST_HIS_EQU")},
        {"Eri", "Eridanus", LocalizationSupport.getMessage("CONST_ERI"), LocalizationSupport.getMessage("CONST_HIS_ERI")},
        {"For", "Fornax", LocalizationSupport.getMessage("CONST_FOR"), LocalizationSupport.getMessage("CONST_HIS_FOR")},
        {"Gem", "Gemini", LocalizationSupport.getMessage("CONST_GEM"), LocalizationSupport.getMessage("CONST_HIS_GEM")},
        {"Gru", "Grus", LocalizationSupport.getMessage("CONST_GRU"), LocalizationSupport.getMessage("CONST_HIS_GRU")},
        {"Her", "Hercules", LocalizationSupport.getMessage("CONST_HER"), LocalizationSupport.getMessage("CONST_HIS_HER")},
        {"Hor", "Horologium", LocalizationSupport.getMessage("CONST_HOR"), LocalizationSupport.getMessage("CONST_HIS_HOR")},
        {"Hya", "Hydra", LocalizationSupport.getMessage("CONST_HYA"), LocalizationSupport.getMessage("CONST_HIS_HYA")},
        {"Hyi", "Hydrus", LocalizationSupport.getMessage("CONST_HYI"), LocalizationSupport.getMessage("CONST_HIS_HYI")},
        {"Ind", "Indus", LocalizationSupport.getMessage("CONST_IND"), LocalizationSupport.getMessage("CONST_HIS_IND")},
        {"Lac", "Lacerta", LocalizationSupport.getMessage("CONST_LAC"), LocalizationSupport.getMessage("CONST_HIS_LAC")},
        {"Leo", "Leo", LocalizationSupport.getMessage("CONST_LEO"), LocalizationSupport.getMessage("CONST_HIS_LEO")},
        {"LMi", "Leo Minor", LocalizationSupport.getMessage("CONST_LMI"), LocalizationSupport.getMessage("CONST_HIS_LMI")},
        {"Lep", "Lepus", LocalizationSupport.getMessage("CONST_LEP"), LocalizationSupport.getMessage("CONST_HIS_LEP")},
        {"Lib", "Libra", LocalizationSupport.getMessage("CONST_LIB"), LocalizationSupport.getMessage("CONST_HIS_LIB")},
        {"Lup", "Lupus", LocalizationSupport.getMessage("CONST_LUP"), LocalizationSupport.getMessage("CONST_HIS_LUP")},
        {"Lyn", "Lynx", LocalizationSupport.getMessage("CONST_LYN"), LocalizationSupport.getMessage("CONST_HIS_LYN")},
        {"Lyr", "Lyra", LocalizationSupport.getMessage("CONST_LYR"), LocalizationSupport.getMessage("CONST_HIS_LYR")},
        {"Men", "Mensa", LocalizationSupport.getMessage("CONST_MEN"), LocalizationSupport.getMessage("CONST_HIS_MEN")},
        {"Mic", "Microscopium", LocalizationSupport.getMessage("CONST_MIC"), LocalizationSupport.getMessage("CONST_HIS_MIC")},
        {"Mon", "Monoceros", LocalizationSupport.getMessage("CONST_MON"), LocalizationSupport.getMessage("CONST_HIS_MON")},
        {"Mus", "Musca", LocalizationSupport.getMessage("CONST_MUS"), LocalizationSupport.getMessage("CONST_HIS_MUS")},
        {"Nor", "Norma", LocalizationSupport.getMessage("CONST_NOR"), LocalizationSupport.getMessage("CONST_HIS_NOR")},
        {"Oct", "Octans", LocalizationSupport.getMessage("CONST_OCT"), LocalizationSupport.getMessage("CONST_HIS_OCT")},
        {"Oph", "Ophiuchus", LocalizationSupport.getMessage("CONST_OPH"), LocalizationSupport.getMessage("CONST_HIS_OPH")},
        {"Ori", "Orion", LocalizationSupport.getMessage("CONST_ORI"), LocalizationSupport.getMessage("CONST_HIS_ORI")},
        {"Pav", "Pavo", LocalizationSupport.getMessage("CONST_PAV"), LocalizationSupport.getMessage("CONST_HIS_PAV")},
        {"Peg", "Pegasus", LocalizationSupport.getMessage("CONST_PEG"), LocalizationSupport.getMessage("CONST_HIS_PEG")},
        {"Per", "Perseus", LocalizationSupport.getMessage("CONST_PER"), LocalizationSupport.getMessage("CONST_HIS_PER")},
        {"Phe", "Phoenix", LocalizationSupport.getMessage("CONST_PHE"), LocalizationSupport.getMessage("CONST_HIS_PHE")},
        {"Pic", "Pictor", LocalizationSupport.getMessage("CONST_PIC"), LocalizationSupport.getMessage("CONST_HIS_PIC")},
        {"Psc", "Pisces", LocalizationSupport.getMessage("CONST_PSC"), LocalizationSupport.getMessage("CONST_HIS_PSC")},
        {"PsA", "Piscis Austrinus", LocalizationSupport.getMessage("CONST_PSA"), LocalizationSupport.getMessage("CONST_HIS_PSA")},
        {"Pup", "Puppis", LocalizationSupport.getMessage("CONST_PUP"), LocalizationSupport.getMessage("CONST_HIS_PUP")},
        {"Pyx", "Pyxis", LocalizationSupport.getMessage("CONST_PYX"), LocalizationSupport.getMessage("CONST_HIS_PYX")},
        {"Ret", "Reticulum", LocalizationSupport.getMessage("CONST_RET"), LocalizationSupport.getMessage("CONST_HIS_RET")},
        {"Sge", "Sagitta", LocalizationSupport.getMessage("CONST_SGE"), LocalizationSupport.getMessage("CONST_HIS_SGE")},
        {"Sgr", "Sagittarius", LocalizationSupport.getMessage("CONST_SGR"), LocalizationSupport.getMessage("CONST_HIS_SGR")},
        {"Sco", "Scorpius", LocalizationSupport.getMessage("CONST_SCO"), LocalizationSupport.getMessage("CONST_HIS_SCO")},
        {"Scl", "Sculptor", LocalizationSupport.getMessage("CONST_SCL"), LocalizationSupport.getMessage("CONST_HIS_SCL")},
        {"Sct", "Scutum", LocalizationSupport.getMessage("CONST_SCT"), LocalizationSupport.getMessage("CONST_HIS_SCT")},
        {"Ser", "Serpens", LocalizationSupport.getMessage("CONST_SER"), LocalizationSupport.getMessage("CONST_HIS_SER")},
        {"Sex", "Sextans", LocalizationSupport.getMessage("CONST_SEX"), LocalizationSupport.getMessage("CONST_HIS_SEX")},
        {"Tau", "Taurus", LocalizationSupport.getMessage("CONST_TAU"), LocalizationSupport.getMessage("CONST_HIS_TAU")},
        {"Tel", "Telescopium", LocalizationSupport.getMessage("CONST_TEL"), LocalizationSupport.getMessage("CONST_HIS_TEL")},
        {"Tri", "Triangulum", LocalizationSupport.getMessage("CONST_TRI"), LocalizationSupport.getMessage("CONST_HIS_TRI")},
        {"TrA", "Triangulum Australe", LocalizationSupport.getMessage("CONST_TRA"), LocalizationSupport.getMessage("CONST_HIS_TRA")},
        {"Tuc", "Tucana", LocalizationSupport.getMessage("CONST_TUC"), LocalizationSupport.getMessage("CONST_HIS_TUC")},
        {"UMa", "Ursa Major", LocalizationSupport.getMessage("CONST_UMA"), LocalizationSupport.getMessage("CONST_HIS_UMA")},
        {"UMi", "Ursa Minor", LocalizationSupport.getMessage("CONST_UMI"), LocalizationSupport.getMessage("CONST_HIS_UMI")},
        {"Vel", "Vela", LocalizationSupport.getMessage("CONST_VEL"), LocalizationSupport.getMessage("CONST_HIS_VEL")},
        {"Vir", "Virgo", LocalizationSupport.getMessage("CONST_VIR"), LocalizationSupport.getMessage("CONST_HIS_VIR")},
        {"Vol", "Volans", LocalizationSupport.getMessage("CONST_VOL"), LocalizationSupport.getMessage("CONST_HIS_VOL")},
        {"Vul", "Vulpecula", LocalizationSupport.getMessage("CONST_VUL"), LocalizationSupport.getMessage("CONST_HIS_VUL")},
        {"", "", "", ""},
    };

    /**
     * Return the number of constellation in my catalog
     * @return number of constellations
     */
    public static int getNumberOfConstellations() {
        return constellations.length;
    }

    /**
     * Return a reference to a constellation from the index in the constellation table
     * @param i index in the table
     * @return a reference to a constellation
     */
    public static ConstellationObject getConstellation(int i) {
        return constellations[i];
    }

    /**
     * Return the abbrevation name of the constellation
     * @param i the index number of the constellation
     * @return the abbrevation name
     */
    public static String getAbbrName(int i) {
        return constNames[i][0];
    }

    /**
     * Return the name of the constellation
     * @return the name of the constellation
     */
    public static String getLatinName(int i) {
        return constNames[i][LATIN];
    }

    /**
     * Return the history text of the constellation
     * @param i the index number of the constellation
     * @return the history of the constellation
     */
    public static String getHistory(int i) {
        return constNames[i][HIST];
    }

    /**
     * Return the number of constellations names
     * @return Number of constellations
     */
    public static int size() {
        return constNames.length;
    }
}
