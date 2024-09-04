@All @DbOpsAll
Feature: DB operations
  DB operation steps

  @DbOps01 @DBSerial
  Scenario: 01 update DB with CSV and validate - SUCCESS - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario1/input/"
    Then Expect 'testData/DbOps/scenario1/output/' with ignore columns
      | user_id |

  @DbOps02 @DBSerial
  Scenario: 02 update DB with CSV and validate - ERROR - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario2/input/"
    Then Expect 'testData/DbOps/scenario2/output/' with ignore columns
      | user_id |

  @DbOps03 @DBSerial
  Scenario: 03 update DB with CSV and validate - SUCCESS - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario3/input/"
    Then Expect 'testData/DbOps/scenario3/output/'

  @DbOps04 @DBSerial
  Scenario: 04 update DB with CSV and validate - ERROR - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario4/input/"
    Then Expect 'testData/DbOps/scenario4/output/'

  @DbOps05 @DBParallel
  Scenario: 05 Use select query to validate DB table - return 1 row - Success
    Then validate data exist for select query "select * from users where name = 'abhishek kadavil'"

  @DbOps06 @DBParallel
  Scenario: 06 Use select query to validate DB table - return 0 row - Fail
    Then validate data exist for select query "select * from users where name = 'abhishek kadavil1'"

  @DbOps07 @DBParallel
  Scenario: 07 Use select query to validate DB table - return 0 row - Success
    Then validate data does not exist for select query "select * from users where name = 'abhishek kadavil1'"

  @DbOps08 @DBParallel
  Scenario: 08 Use select query to validate DB table - return 1 row - Fail
    Then validate data does not exist for select query "select * from users where name = 'abhishek kadavil'"

  @DbOps09 @DBParallel
  Scenario: 09 Use select query to validate DB table from file - return 1 row - Success
    Then validate data exist for select query from file "/DbOps/scenario9/SelQuery.sql"

  @DbOps10 @DBParallel
  Scenario: 10 Use select query to validate DB table from file - return 0 row - Fail
    Then validate data exist for select query from file "/DbOps/scenario10/SelQuery.sql"

  @DbOps11 @DBParallel
  Scenario: 11 Use select query to validate DB table from file - return 0 row - Success
    Then validate data does not exist for select query from file "/DbOps/scenario11/SelQuery.sql"

  @DbOps12 @DBParallel
  Scenario: 12 Use select query to validate DB table from file - return 1 row - Fail
    Then validate data does not exist for select query from file "/DbOps/scenario12/SelQuery.sql"

  @DbOps13 @DBParallel
  Scenario: 13 Use select query and context value to validate DB table - return 1 row - Success
    * put 'abhishek kadavil' in scenario context value 'name'
    Then validate data exist for select query 'select * from users where name = ' and where condition as a context value 'name'

  @DbOps14 @DBParallel
  Scenario: 14 Use select query and context value to validate DB table - return 0 row - Fail
    * put 'abhishek kadavil1' in scenario context value 'name'
    Then validate data exist for select query 'select * from users where name = ' and where condition as a context value 'name'

  @DbOps15 @DBParallel
  Scenario: 15 Use select query and context value to validate DB table - return 0 row - Success
    * put 'abhishek kadavil1' in scenario context value 'name'
    Then validate data not exist for select query 'select * from users where name = ' and where condition as a context value 'name'

  @DbOps16 @DBParallel
  Scenario: 16 Use select query and context value to validate DB table - return 1 row - Fail
    * put 'abhishek kadavil' in scenario context value 'name'
    Then validate data not exist for select query 'select * from users where name = ' and where condition as a context value 'name'

  @DbOps17 @DBParallel
  Scenario: 17 Use select query from file and context value to validate DB table - return 1 row - Success
    * put 'abhishek kadavil' in scenario context value 'name'
    Then validate data exist for select query from file "/DbOps/scenario17/SelQuery.sql" and context value 'name'

  @DbOps18 @DBParallel
  Scenario: 18 Use select query from file and context value to validate DB table - return 0 row - fail
    * put 'abhishek kadavil1' in scenario context value 'name'
    Then validate data exist for select query from file "/DbOps/scenario18/SelQuery.sql" and context value 'name'

  @DbOps19 @DBParallel
  Scenario: 19 Use select query from file and context value to validate DB table - return 0 row - Success
    * put 'abhishek kadavil1' in scenario context value 'name'
    Then validate data not exist for select query from file "/DbOps/scenario17/SelQuery.sql" and context value 'name'

  @DbOps20 @DBParallel
  Scenario: 20 Use select query from file and context value to validate DB table - return 1 row - fail
    * put 'abhishek kadavil' in scenario context value 'name'
    Then validate data not exist for select query from file "/DbOps/scenario18/SelQuery.sql" and context value 'name'