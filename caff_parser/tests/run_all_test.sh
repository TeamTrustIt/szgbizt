echo "=================="
echo "Running all tests"
echo "=================="

echo
echo "---- Positive tests: ----"
echo "* Test 1.caff file:"
../caff_parser.exe ./positive_parse_test/input_files/1.caff ./positive_parse_test/output_files/1.json
echo
echo "* Test 2.caff file:"
../caff_parser.exe ./positive_parse_test/input_files/2.caff ./positive_parse_test/output_files/2.json
echo
echo "* Test 3.caff file:"
../caff_parser.exe ./positive_parse_test/input_files/3.caff ./positive_parse_test/output_files/3.json
echo "-------------------------"


echo
echo "---- Negative tests: ----"
echo "* Test file which doesn't start with header:"
../caff_parser.exe ./negative_parse_test/input_files/doesnt_start_with_header.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with line break in tags:"
../caff_parser.exe ./negative_parse_test/input_files/line_break_in_tags.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with missing block:"
../caff_parser.exe ./negative_parse_test/input_files/missing_block.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with more animation blocks:"
../caff_parser.exe ./negative_parse_test/input_files/more_animations.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with more bytes:"
../caff_parser.exe ./negative_parse_test/input_files/more_bytes.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with no tag end:"
../caff_parser.exe ./negative_parse_test/input_files/no_tag_end.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with not 'CAFF' magic:"
../caff_parser.exe ./negative_parse_test/input_files/not_caff.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with not 'CIFF' magic:"
../caff_parser.exe ./negative_parse_test/input_files/not_ciff.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with random errors:"
../caff_parser.exe ./negative_parse_test/input_files/random_errors.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with smaller animation size:"
../caff_parser.exe ./negative_parse_test/input_files/smaller_animation_size.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with smaller content size:"
../caff_parser.exe ./negative_parse_test/input_files/smaller_content_size.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with smaller credits size:"
../caff_parser.exe ./negative_parse_test/input_files/smaller_credits_size.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with smaller header size:"
../caff_parser.exe ./negative_parse_test/input_files/smaller_header_size.caff ./negative_parse_test/output_files/should_not_happen.json
echo
echo "* Test file with unknown block:"
../caff_parser.exe ./negative_parse_test/input_files/unknown_block.caff ./negative_parse_test/output_files/should_not_happen.json
echo "-------------------------"
echo