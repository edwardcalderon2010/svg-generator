#!/bin/bash

tgt_files="";

exc_key="exc";

master_file="all_paths.xml";
temp_file="temp_1.txt";

init() {
	tgt_files=(`ls -C1 | grep svg | grep -v "^[0-9]"`);
}

convert() {
	for tgt_file in "${tgt_files[@]}"
	do
		tmp_char=`echo $tgt_file | awk -F "_" '{print $1}'`;
		tmp_suffix=`echo $tgt_file | sed 's/^[^_]\+//g' `;
		tmp_repl="";
		tmp_convert="";
		
		if [ "${#tmp_char}" -eq 1 ]; then
			tmp_convert=`echo -n $tmp_char | od -An -t uC | sed 's/\o40//g'`;
		else
			if [ "$tmp_char" = "$exc_key" ]; then
				tmp_convert="033";
			fi
		fi
		
		if [ "${#tmp_convert}" -gt 0 ]; then
			
			if [ "${#tmp_convert}" -lt 3 ]; then
				tmp_convert="0$tmp_convert";
			fi
			
			tmp_repl=$tmp_convert$tmp_suffix;
			echo "converting: $tgt_file to $tmp_repl";
			mv $tgt_file $tmp_repl;
		fi
	done
}

generate_master_xml() {
	cat `find . -name "*.svg"` | tr -d "\n" | tr -d "\r" | sed 's/>\s\+</></g' | sed 's/></>\n</g' | grep path > $temp_file;
	echo "<body>" > $master_file;
	echo "<def>" >> $master_file;
	cat $temp_file | grep -v "_mask_" | sort >> $master_file;
	echo "</def>" >> $master_file;

	echo "<masks>" >> $master_file;
	cat $temp_file | grep "_mask_" | sort >> $master_file;
	echo "</masks>" >> $master_file;
	echo "</body>" >> $master_file;

	rm -rf $temp_file;
}

main() {
	init;
	#convert;
	generate_master_xml;
}

main;