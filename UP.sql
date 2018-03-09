CREATE TABLE dbo.UserAccounts
(
  UserID  NVARCHAR(50)  NOT NULL, /*Populated with Google SID On first login*/
  Username  NVARCHAR(20)  NOT NULL,
  Timezone  NVARCHAR(5) NULL,
  TimeFormat  NVARCHAR(5) NULL,
  Email NVARCHAR(50)  NOT NULL

  CONSTRAINT[PK_dbo.UserAccounts] PRIMARY KEY (UserID)
);

GO
