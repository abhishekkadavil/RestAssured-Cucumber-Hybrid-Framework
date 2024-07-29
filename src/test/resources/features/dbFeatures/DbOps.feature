Feature: DB operations
  DB operation steps

  @DbOps01 @DbOpsAll
  Scenario: 01 update DB with CSV and validate - SUCCESS - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario1/input/"
    Then Expect 'testData/DbOps/scenario1/output/' with ignore columns
      | user_id |

  @DbOps02 @DbOpsAll
  Scenario: 02 update DB with CSV and validate - ERROR - with ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario2/input/"
    Then Expect 'testData/DbOps/scenario2/output/' with ignore columns
      | user_id |

  @DbOps03 @DbOpsAll
  Scenario: 03 update DB with CSV and validate - SUCCESS - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario3/input/"
    Then Expect 'testData/DbOps/scenario3/output/'

  @DbOps04 @DbOpsAll
  Scenario: 04 update DB with CSV and validate - ERROR - without ignore columns
    Given execute statement "delete from users"
    Given db is updated with "testData/DbOps/scenario4/input/"
    Then Expect 'testData/DbOps/scenario4/output/'

  @DbOps05 @DBParallel @DbOpsAll
  Scenario: 05 Use select query to validate DB table - return 1 row - Success
    Then validate data exist for select query "select * from users where name = 'abhishek kadavil'"

  @DbOps06 @DBParallel @DbOpsAll
  Scenario: 06 Use select query to validate DB table - return 0 row - Fail
    Then validate data exist for select query "select * from users where name = 'abhishek kadavil1'"

  @DbOps07 @DBParallel @DbOpsAll
  Scenario: 07 Use select query to validate DB table - return 0 row - Success
    Then validate data does not exist for select query "select * from users where name = 'abhishek kadavil1'"

  @DbOps08 @DBParallel @DbOpsAll
  Scenario: 08 Use select query to validate DB table - return 1 row - Success
    Then validate data does not exist for select query "select * from users where name = 'abhishek kadavil'"