import re
import numpy as np
import sys

app = "toyapp-timeline.txt"

if len(sys.argv) > 1:
    app = sys.argv[1]



def living_extraction(file_name):
    pattern = r"# Living instances: \((\d+)\)"
    valori = []

    with open(file_name, 'r') as file:
        for line in file:
            match = re.search(pattern, line)
            if match:
                valori.append(int(match.group(1)))

    return valori

def request_extraction(file_name):
    pattern = r"#   Request: (\d+\.\d+) ms \((\d+\.\d+) %\)"
    val = []
    perc = []

    with open(file_name, 'r') as file:
        for line in file:
            match = re.search(pattern, line)
            if match:
                val.append(float(match.group(1)))
                perc.append(float(match.group(2)))

    return val, perc

def method_extraction(file_name):
    pattern = r"# Method calls: \((\d+)\)"
    valori = []

    with open(file_name, 'r') as file:
        for line in file:
            match = re.search(pattern, line)
            if match:
                valori.append(int(match.group(1)))

    return valori

def method_overhead_extraction(file_name):
    pattern = r"#   Methods: (\d+\.\d+) ms \((\d+\.\d+) %\)"
    val = []
    perc = []

    with open(file_name, 'r') as file:
        for line in file:
            match = re.search(pattern, line)
            if match:
                val.append(float(match.group(1)))
                perc.append(float(match.group(2)))

    return val, perc

def overall_overhead_extraction(file_name):
    pattern = r"# Overhead: (\d+\.\d+) ms \((\d+\.\d+) %\)"
    val = []
    perc = []

    with open(file_name, 'r') as file:
        for line in file:
            match = re.search(pattern, line)
            if match:
                val.append(float(match.group(1)))
                perc.append(float(match.group(2)))

    return val, perc


living = living_extraction(app)

print("Mean living instances per request of " + app + ": " + str(np.mean(living)))
print("STD living instances per request of " + app + ": " + str(np.std(living)))

print("")

request_absolute, request_perc = request_extraction(app);

print("Mean request overhead of " + app + ": " + str(np.mean(request_absolute)))
print("STD request overhead of " + app + ": " + str(np.std(request_absolute)))

print("")

methods_req = method_extraction(app)

print("Mean methods per request of " + app + ": " + str(np.mean(methods_req)))
print("STD methods per request of " + app + ": " + str(np.std(methods_req)))


print("")
method_ov_absolute, method_ov_perc = method_overhead_extraction(app);


print("Mean methods overhead per request of " + app + ": " + str(np.mean(method_ov_absolute)))
print("STD methods overhead per request of " + app + ": " + str(np.std(method_ov_absolute)))



print("")
overall_ov_absolute, overall_ov_perc = overall_overhead_extraction(app);

