# MOCDroid

# UNDER CONSTRUCTION


MOCDroid is a multi-objective evolutionary classifier for Android malware detection


# Abstract
Malware threats are growing, while at the same time, concealment strategies are being used to make them undetectable for current commercial antivirus. Android is one of the target architectures where these problems are specially alarming due to the wide extension of the platform in different everyday devices. The detection is specially relevant for Android markets in order to ensure that all the software they offer is clean. However, obfuscation has proven to be effective at evading the detection process. In this paper, we leverage third-party calls to bypass the effects of these concealment strategies, since they cannot be obfuscated. We combine clustering and multi-objective optimisation to generate a classifier based on specific behaviours defined by third-party call groups. The optimiser ensures that these groups are related to malicious or benign behaviours cleaning any non-discriminative pattern. This tool, named MOCDroid, achieves an accuracy of 95.15 % in test with 1.69 % of false positives with real apps extracted from the wild, overcoming all commercial antivirus engines from VirusTotal.



MOCDroid is described in:

Martín, A., Menéndez, H. D., & Camacho, D. (2016). MOCDroid: multi-objective evolutionary classifier for Android malware detection. Soft Computing, 1-11.
http://link.springer.com/article/10.1007/s00500-016-2283-y

Please, if you use this software, cite the above paper.
